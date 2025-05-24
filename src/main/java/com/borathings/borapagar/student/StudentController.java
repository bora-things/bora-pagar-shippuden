package com.borathings.borapagar.student;

import com.borathings.borapagar.student.dto.StudentResponseDTO;
import com.borathings.borapagar.subject.dto.ComponentDTO;
import java.util.List;
// Can be useful for default pageable
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/students")
public interface StudentController {

    @GetMapping("/me")
    public ResponseEntity<StudentResponseDTO> currentStudent(Authentication currentUser);

    @GetMapping("/me/possible-subjects")
    public ResponseEntity<List<ComponentDTO>> getPossibleSubjects(
            Authentication currentUser, @PageableDefault(size = 20, page = 0) Pageable pageable);

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getById(@PathVariable Long studentId);
}
