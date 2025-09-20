package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.student.interest.dto.FriendsInterestsDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectAddInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Importação correta

@RequestMapping("/students")
public interface StudentSubjectInterestController {

    @GetMapping("/me/interests")
    public ResponseEntity<List<StudentSubjectInterestDTO>> listMyInterests(Authentication currentUser);

    @GetMapping("/{userId}/interests")
    public ResponseEntity<List<StudentSubjectInterestDTO>> listInterestsByUserId(
            Authentication authentication, @PathVariable("userId") Long userId);

    @PostMapping("/me/interests")
    public ResponseEntity<Void> createInterest(
            @RequestBody StudentSubjectAddInterestDTO semesterDTO, Authentication currentUser);

    @DeleteMapping("/me/interests/{subjectCode}")
    public ResponseEntity<Void> removeInterest(@PathVariable String subjectCode, Authentication currentUser);

    @GetMapping("/me/interests/friends")
    public ResponseEntity<FriendsInterestsDTO> listFriendsInterests(
            Authentication currentUser, @RequestParam("period") Integer period, @RequestParam("year") Integer year);
}
