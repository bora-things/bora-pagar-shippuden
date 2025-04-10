package com.borathings.borapagar.student;

import com.borathings.borapagar.student.dto.StudentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/students")
public interface StudentController {

    @GetMapping("/me")
    public ResponseEntity<StudentDTO> currentStudent(Authentication currentUser);

}
