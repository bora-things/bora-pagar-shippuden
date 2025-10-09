package com.borathings.borapagar.student;

import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import com.borathings.borapagar.student.dto.SearchedStudentResponseDTO;
import com.borathings.borapagar.student.dto.StudentResponseDTO;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ResponseEntity<SearchedStudentResponseDTO> getById(Authentication authentication, Long studentId) {
        String userLogin = authentication.getName();
        SearchedStudentResponseDTO s = studentService.findStudentResponseDTOById(userLogin, studentId);

        return ResponseEntity.ok(s);
    }

    @Override
    public ResponseEntity<Page<SearchedStudentResponseDTO>> getAllStudents(
            Authentication authentication, String studentName, Pageable pageable) {
        String userLogin = authentication.getName();
        Page<SearchedStudentResponseDTO> response = studentService.findStudentsByName(userLogin, studentName, pageable);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Page<SearchedStudentResponseDTO>> getAllFriends(
            Authentication authentication, String studentName, Pageable pageable) {
        String userLogin = authentication.getName();
        Page<SearchedStudentResponseDTO> response = studentService.findFriendsByName(userLogin, studentName, pageable);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<ClassroomResponseDTO>> getPossibleSubjects(
            Authentication currentUser, Pageable pageable) {
        String userLogin = currentUser.getName();
        List<ClassroomResponseDTO> subjects = studentService.getPossibleSubjectsForStudent(userLogin, pageable);
        return ResponseEntity.ok(subjects);
    }
}
