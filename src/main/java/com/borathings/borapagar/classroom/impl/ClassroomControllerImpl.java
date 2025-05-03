package com.borathings.borapagar.classroom.impl;

import com.borathings.borapagar.classroom.ClassroomController;
import com.borathings.borapagar.classroom.ClassroomService;
import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassroomControllerImpl implements ClassroomController {
    @Autowired
    private ClassroomService classroomService;

    @Override
    public ResponseEntity<List<ClassroomResponseDTO>> findStudentClassroom(Authentication authentication) {
        String login = authentication.getName();
        return ResponseEntity.ok(classroomService.findClassroomByStudent(login));
    }
}
