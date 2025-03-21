package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestSemesterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users/interests")
public interface StudentSubjectInterestController {

    @GetMapping("/")
    public ResponseEntity<Void> listInterests(Authentication currentUser);

    @PostMapping("/{subjectId}")
    public ResponseEntity<Void> createInterest(
            @PathVariable int sigaaSubjectId,
            @RequestBody StudentSubjectInterestSemesterDTO semesterDTO,
            Authentication currentUser);

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> removeInterest(@PathVariable int sigaaSubjectId, Authentication currentUser);
}
