package com.borathings.borapagar.student.interest.impl;

import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.interest.StudentSubjectInterestController;
import com.borathings.borapagar.student.interest.StudentSubjectInterestService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestSemesterDTO;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserService;
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

    public ResponseEntity<Void> listInterests(Authentication currentUser) {

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> createInterest(
            int sigaaSubjectId, StudentSubjectInterestSemesterDTO semesterDTO, Authentication currentUser) {

        UserEntity u = userService.findByLoginOrError(currentUser.getName());
        StudentEntity s = studentService.findByUserIdOrError(u.getUserId());

        interestService.createInterest(sigaaSubjectId, semesterDTO, s);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> removeInterest(int sigaaSubjectId, Authentication currentUser) {

        UserEntity user = userService.findByLoginOrError(currentUser.getName());
        StudentEntity student = studentService.findByUserIdOrError(user.getUserId());

        interestService.deleteInterest(sigaaSubjectId, student);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
