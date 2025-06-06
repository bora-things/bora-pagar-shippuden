package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.SubjectSigaaClient;
import com.borathings.borapagar.component.dto.ComponentDTO;
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

    public List<StudentSubjectInterestEntity> findAllByStudentId(Long studentId) {
        return studentSubjectInterestRepository.findAllByStudentId(studentId);
    }

    public List<StudentSubjectInterestDTO> listInterests(Long studentId) {
        List<StudentSubjectInterestEntity> studentSubjectInterests =
                studentSubjectInterestRepository.findAllByStudentId(studentId);

        StudentEntity student = studentService.findByIdOrError(studentId);
        Set<UserEntity> friends = Optional.ofNullable(student.getUser())
                .map(UserEntity::getFriends)
                .orElse(Set.of());
        Map<Long, UserResponseDTO> friendsMap =
                friends.stream().collect(Collectors.toMap(UserEntity::getId, userMapper::toUserResponseDTO));

        return studentSubjectInterests.stream()
                .map(interest -> {
                    ComponentDTO component = subjectClient.getComponentByCode(interest.getSubjectCode());

                    List<Long> friendsWithSameInterest = findFriendsWithSameInterest(
                            friends, interest.getSubjectCode(), interest.getYear(), interest.getPeriod());

                    return new StudentSubjectInterestDTO(
                            interest.getId(),
                            component,
                            interest.getYear(),
                            interest.getPeriod(),
                            friendsWithSameInterest.stream()
                                    .map(friendsMap::get)
                                    .toList());
                })
                .collect(Collectors.toList());
    }

    private List<Long> findFriendsWithSameInterest(
            Set<UserEntity> friends, String subjectCode, Integer year, Integer period) {
        return friends.stream()
                .map(friend -> studentService.findByIdOrError(friend.getId()))
                .filter(Objects::nonNull)
                .flatMap(friendStudent ->
                        studentSubjectInterestRepository.findAllByStudentId(friendStudent.getId()).stream())
                .filter(friendInterest -> friendInterest.getSubjectCode().equals(subjectCode)
                        && friendInterest.getYear().equals(year)
                        && friendInterest.getPeriod().equals(period))
                .map(StudentSubjectInterestEntity::getId)
                .distinct()
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

        List<ClassroomEntity> studentClasses = student.getClassrooms();
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
