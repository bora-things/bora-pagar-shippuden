package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.SubjectSigaaClient;
import com.borathings.borapagar.component.dto.ComponentDTO;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.core.exception.subjectInterest.InterestInCompletedSubjectException;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentHelperService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectAddInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestDTO;
import com.borathings.borapagar.student.transcript.TranscriptComponentEntity;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserMapper;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentSubjectInterestService {

    @Autowired
    StudentSubjectInterestRepository studentSubjectInterestRepository;

    @Autowired
    StudentHelperService studentService;

    @Autowired
    SubjectSigaaClient subjectClient;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ComponentService componentService;
    @Autowired
    private ComponentMapper componentMapper;

    public List<StudentSubjectInterestEntity> findAllByStudentId(Long studentId) {
        return studentSubjectInterestRepository.findAllByStudentId(studentId);
    }


    public List<StudentSubjectInterestDTO> listInterests(Long studentId) {
        List<StudentSubjectInterestEntity> studentInterests =
                studentSubjectInterestRepository.findAllByStudentId(studentId);

        if (studentInterests.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> subjectCodes = studentInterests.stream()
                .map(StudentSubjectInterestEntity::getSubjectCode)
                .toList();

        Map<String, ComponentEntity> componentMap = componentService.findAllByCodeIn(subjectCodes).stream()
                .collect(Collectors.toMap(
                        ComponentEntity::getCode,
                        component -> component,
                        (existingValue, newValue) -> existingValue
                ));

        StudentEntity student = studentService.findByIdOrError(studentId);
        Set<UserEntity> friends = Optional.ofNullable(student.getUser())
                .map(UserEntity::getFriends)
                .orElse(Set.of());

        Map<List<Object>, List<UserResponseDTO>> friendsByInterestMap = new HashMap<>();

        if (friends != null && !friends.isEmpty()) {
            Map<Long, UserResponseDTO> friendsDtoMap = friends.stream()
                    .collect(Collectors.toMap(UserEntity::getId, userMapper::toUserResponseDTO));

            List<Long> friendIds = new ArrayList<>(friendsDtoMap.keySet());


            List<StudentSubjectInterestEntity> friendsInterests =
                    studentSubjectInterestRepository.findAllByStudentIdIn(friendIds);

            for (StudentSubjectInterestEntity friendInterest : friendsInterests) {
                List<Object> interestKey = List.of(
                        friendInterest.getSubjectCode(),
                        friendInterest.getYear(),
                        friendInterest.getPeriod());

                UserResponseDTO friendDto = friendsDtoMap.get(friendInterest.getStudent().getId());
                if (friendDto != null) {
                    friendsByInterestMap
                            .computeIfAbsent(interestKey, k -> new ArrayList<>())
                            .add(friendDto);
                }
            }
        }

        return studentInterests.stream()
                .map(interest -> {
                    ComponentEntity component = componentMap.get(interest.getSubjectCode());

                    List<Object> currentInterestKey = List.of(
                            interest.getSubjectCode(),
                            interest.getYear(),
                            interest.getPeriod());

                    List<UserResponseDTO> friendsWithSameInterest = friendsByInterestMap
                            .getOrDefault(currentInterestKey, Collections.emptyList());

                    return new StudentSubjectInterestDTO(
                            interest.getId(),
                            componentMapper.toDto(component),
                            interest.getYear(),
                            interest.getPeriod(),
                            friendsWithSameInterest);
                })
                .collect(Collectors.toList());
    }

    public void createInterest(StudentSubjectAddInterestDTO semesterDTO, StudentEntity student) {

        StudentSubjectInterestEntity interestEntity = new StudentSubjectInterestEntity(
                semesterDTO.year(), semesterDTO.period(), student, semesterDTO.subjectCode());

        Optional<ComponentEntity> component = componentService.findByCode(semesterDTO.subjectCode());
        if (component.isEmpty()) {
            throw new EntityNotFoundException("Componente não encontrado");
        }

        List<TranscriptComponentEntity> studentTranscriptComponents = student.getTranscriptComponents();
        if (studentTranscriptComponents.stream()
                .anyMatch(item -> item.getComponentId().equals(component.get().getComponentId()))) {
            throw new InterestInCompletedSubjectException();
        }

        Set<ClassroomEntity> studentClasses = student.getClassrooms();
        if (studentClasses.stream()
                .anyMatch(item -> item.getComponentCode().equals(component.get().getCode()))) {
            throw new InterestInCompletedSubjectException();
        }

        Optional<StudentSubjectInterestEntity> optionalStudentSubjectInterestEntity =
                studentSubjectInterestRepository.findBySubjectCodeAndStudentId(
                        semesterDTO.subjectCode(), student.getId());
        if (optionalStudentSubjectInterestEntity.isPresent()) {
            throw new InterestInCompletedSubjectException();
        }
        studentSubjectInterestRepository.save(interestEntity);
    }
    ;

    public void deleteInterest(String subjectCode, StudentEntity student) {
        studentSubjectInterestRepository.deleteBySigaaSubjectIdAndStudentId(subjectCode, student.getId());
    }
}
