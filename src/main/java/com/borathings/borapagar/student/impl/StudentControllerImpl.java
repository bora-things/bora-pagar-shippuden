package com.borathings.borapagar.student.impl;

import com.borathings.borapagar.student.StudentController;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentMapper;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentControllerImpl implements StudentController {

    @Autowired
    StudentService studentService;

    public ResponseEntity<StudentDTO> currentStudent(Authentication currentUser) {
        StudentDTO s = studentService.getCurrentStudent(currentUser.getName());
        return ResponseEntity.ok(s);
    }

}
