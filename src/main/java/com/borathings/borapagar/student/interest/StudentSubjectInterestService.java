package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.component.SubjectSigaaClient;
import com.borathings.borapagar.component.dto.ComponentDTO;
import com.borathings.borapagar.core.exception.subjectInterest.SubjectInterestAlreadyExistsException;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentHelperService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectAddInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestDTO;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserMapper;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
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
        try {
            studentSubjectInterestRepository.save(interestEntity);
        } catch (Exception e) {
            throw new SubjectInterestAlreadyExistsException();
        }
    }
    ;

    public void deleteInterest(String subjectCode, StudentEntity student) {
        studentSubjectInterestRepository.deleteBySigaaSubjectIdAndStudentId(subjectCode, student.getId());
    }
}
