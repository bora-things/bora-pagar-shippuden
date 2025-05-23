package com.borathings.borapagar.student.interest.impl;

import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.interest.StudentSubjectInterestController;
import com.borathings.borapagar.student.interest.StudentSubjectInterestService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectAddInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestDTO;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentSubjectInterestControllerImpl implements StudentSubjectInterestController {

    @Autowired
    StudentSubjectInterestService interestService;

    @Autowired
    UserService userService;

    @Autowired
    StudentService studentService;

    public ResponseEntity<List<StudentSubjectInterestDTO>> listInterests(Authentication currentUser) {
        UserEntity user = userService.findByLoginOrError(currentUser.getName());
        StudentEntity student = studentService.findByUserIdOrError(user.getUserId());
        List<StudentSubjectInterestDTO> interests = interestService.listInterests(student.getId());

        return ResponseEntity.ok(interests);
    }

    public ResponseEntity<Void> createInterest(StudentSubjectAddInterestDTO semesterDTO, Authentication currentUser) {

        UserEntity u = userService.findByLoginOrError(currentUser.getName());
        StudentEntity s = studentService.findByUserIdOrError(u.getUserId());

        interestService.createInterest(semesterDTO, s);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> removeInterest(int subjectId, Authentication currentUser) {

        UserEntity user = userService.findByLoginOrError(currentUser.getName());
        StudentEntity student = studentService.findByUserIdOrError(user.getUserId());

        interestService.deleteInterest(subjectId, student);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
