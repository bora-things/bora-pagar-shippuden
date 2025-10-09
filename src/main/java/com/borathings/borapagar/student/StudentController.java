package com.borathings.borapagar.student;

import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import com.borathings.borapagar.student.dto.SearchedStudentResponseDTO;
import com.borathings.borapagar.student.dto.StudentResponseDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/students")
public interface StudentController {

    @GetMapping("/me")
    public ResponseEntity<StudentResponseDTO> currentStudent(Authentication currentUser);

    @GetMapping("/me/possible-subjects")
    public ResponseEntity<List<ClassroomResponseDTO>> getPossibleSubjects(
            Authentication currentUser, @PageableDefault(size = 20, page = 0) Pageable pageable);

    @GetMapping("/{studentId}")
    public ResponseEntity<SearchedStudentResponseDTO> getById(
            Authentication authentication, @PathVariable Long studentId);

    @GetMapping
    public ResponseEntity<Page<SearchedStudentResponseDTO>> getAllStudents(
            Authentication authentication,
            @RequestParam(required = false) String studentName,
            @PageableDefault(size = 10, page = 0, sort = "studentName", direction = Sort.Direction.ASC)
                    Pageable pageable);

    @GetMapping("/me/friends")
    public ResponseEntity<Page<SearchedStudentResponseDTO>> getAllFriends(
            Authentication authentication,
            @RequestParam(required = false) String studentName,
            @PageableDefault(size = 10, page = 0, sort = "studentName", direction = Sort.Direction.ASC)
                    Pageable pageable);
}
