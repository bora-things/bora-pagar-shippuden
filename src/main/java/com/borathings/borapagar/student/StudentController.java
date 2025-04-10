package com.borathings.borapagar.student;

import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.student.dto.StudentInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/students")
public interface StudentController {

    @GetMapping("/me")
    public ResponseEntity<StudentDTO> currentStudent(Authentication currentUser);

    @GetMapping("/me/info")
    public ResponseEntity<StudentInfoDto> currentStudentInfo(Authentication currentUser);
}
