package com.borathings.borapagar.classroom;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@RequestMapping("/classrooms")
public interface ClassroomController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public ResponseEntity<List<ClassroomResponseDTO>> findStudentClassroom(Authentication authentication);


}
