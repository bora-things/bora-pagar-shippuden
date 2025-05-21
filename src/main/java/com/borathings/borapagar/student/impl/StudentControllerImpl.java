package com.borathings.borapagar.student.impl;

import com.borathings.borapagar.student.StudentController;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.dto.StudentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentControllerImpl implements StudentController {

    @Autowired
    StudentService studentService;

    public ResponseEntity<StudentResponseDTO> currentStudent(Authentication currentUser) {
        StudentResponseDTO s = studentService.getCurrentStudent(currentUser.getName());
        return ResponseEntity.ok(s);
    }

    public ResponseEntity<StudentResponseDTO> getById(Long studentId) {
        StudentResponseDTO s = studentService.findStudentResponseDTOById(studentId);
        return ResponseEntity.ok(s);
    }
}
