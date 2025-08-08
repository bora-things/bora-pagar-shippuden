package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.core.persistence.AbstractModel;
import com.borathings.borapagar.student.interest.exception.InterestInCompletedSubjectException;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentHelperService;
import com.borathings.borapagar.student.interest.dto.FriendsInterestsDTO;
import com.borathings.borapagar.student.interest.dto.StudentFriendInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectAddInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestDTO;
import com.borathings.borapagar.student.interest.util.RequisiteParser;
import com.borathings.borapagar.student.transcript.TranscriptComponentEntity;
import com.borathings.borapagar.student.transcript.enums.TranscriptComponentSituationEnum;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserMapper;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentSubjectInterestService {

    StudentSubjectInterestRepository studentSubjectInterestRepository;
    StudentHelperService studentService;
    UserMapper userMapper;
    ComponentService componentService;
    ComponentMapper componentMapper;

    public List<StudentSubjectInterestEntity> getFriendsInterestsInComponent(StudentEntity student, String code) {
        List<Long> friendsIds = student.getUser().getFriends().stream()
                .map(AbstractModel::getId)
                .toList();
        return studentSubjectInterestRepository.findAllBySubjectCodeAndStudentIn(code, friendsIds);
    }

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
                        ComponentEntity::getCode, component -> component, (existingValue, newValue) -> existingValue));

        StudentEntity student = studentService.findByIdOrError(studentId);
        Set<UserEntity> friends = Optional.ofNullable(student.getUser())
                .map(UserEntity::getFriends)
                .orElse(Set.of());

        Map<List<Object>, List<UserResponseDTO>> friendsByInterestMap = new HashMap<>();

        if (friends != null && !friends.isEmpty()) {
            Map<Long, UserResponseDTO> friendsDtoMap =
                    friends.stream().collect(Collectors.toMap(UserEntity::getId, userMapper::toUserResponseDTO));

            List<Long> friendIds = new ArrayList<>(friendsDtoMap.keySet());

            List<StudentSubjectInterestEntity> friendsInterests =
                    studentSubjectInterestRepository.findAllByStudentIdIn(friendIds);

            for (StudentSubjectInterestEntity friendInterest : friendsInterests) {
                List<Object> interestKey =
                        List.of(friendInterest.getSubjectCode(), friendInterest.getYear(), friendInterest.getPeriod());

                UserResponseDTO friendDto =
                        friendsDtoMap.get(friendInterest.getStudent().getId());
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

                    List<Object> currentInterestKey =
                            List.of(interest.getSubjectCode(), interest.getYear(), interest.getPeriod());

                    List<UserResponseDTO> friendsWithSameInterest =
                            friendsByInterestMap.getOrDefault(currentInterestKey, Collections.emptyList());

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

        ComponentEntity component = componentService
                .findByCode(semesterDTO.subjectCode())
                .orElseThrow(
                        () -> new EntityNotFoundException("Componente não encontrado: " + semesterDTO.subjectCode()));

        List<ComponentEntity> transcriptComponents =
                componentService.findAllByComponentId(student.getTranscriptComponents().stream()
                        .map(TranscriptComponentEntity::getComponentId)
                        .toList());

        Set<String> completedOrEnrolledCodes = Stream.concat(
                        transcriptComponents.stream().map(ComponentEntity::getCode),
                        student.getClassrooms().stream().map(ClassroomEntity::getComponentCode))
                .collect(Collectors.toSet());
        System.out.println(completedOrEnrolledCodes);

        if (completedOrEnrolledCodes.contains(component.getCode())) {
            throw new InterestInCompletedSubjectException();
        }

        if (component.getPreRequisites() != null
                && !component.getPreRequisites().isEmpty()) {
            RequisiteParser parser = new RequisiteParser(completedOrEnrolledCodes);
            parser.assertRequisitesAreMet(component.getPreRequisites());
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

    public FriendsInterestsDTO listFriendsInterests(StudentEntity student, Integer period, Integer year) {

        Set<UserEntity> userFriends = student.getUser().getFriends();
        List<StudentSubjectInterestEntity> friendsInterests =
                studentSubjectInterestRepository.findAllByUserInAndPeriodAndYear(userFriends, period, year);

        List<String> uniqueSubjectCodes = friendsInterests.stream()
                .map(StudentSubjectInterestEntity::getSubjectCode)
                .distinct()
                .toList();

        List<ComponentEntity> components = componentService.findAllByCodeIn(uniqueSubjectCodes);

        List<ComponentResponseDTO> componentResponseDTOS = components.stream()
                .map(item -> componentMapper.toResponseDTO(item))
                .toList();

        Map<Integer, String> componentMap = components.stream()
                .collect(Collectors.toMap(ComponentEntity::getComponentId, ComponentEntity::getCode, (a, b) -> a));

        Map<StudentEntity, List<StudentSubjectInterestEntity>> groupedByStudent =
                friendsInterests.stream().collect(Collectors.groupingBy(StudentSubjectInterestEntity::getStudent));

        List<StudentFriendInterestDTO> studentFriendInterestDTOS = groupedByStudent.entrySet().stream()
                .map(entry -> {
                    StudentEntity s = entry.getKey();
                    List<StudentSubjectInterestEntity> interests = entry.getValue();

                    List<String> subjectInterestsCodes = interests.stream()
                            .map(StudentSubjectInterestEntity::getSubjectCode)
                            .distinct()
                            .toList();
                    List<String> subjectsFinished = s.getTranscriptComponents().stream()
                            .filter(item -> TranscriptComponentSituationEnum.fromId(item.getSituation())
                                    .isApproved())
                            .map(item -> componentMap.get(item.getComponentId()))
                            .filter(Objects::nonNull)
                            .toList();

                    return new StudentFriendInterestDTO(
                            s.getStudentName(), s.getImageUrl(), subjectInterestsCodes, subjectsFinished);
                })
                .toList();

        return new FriendsInterestsDTO(componentResponseDTOS, studentFriendInterestDTOS);
    }
}
