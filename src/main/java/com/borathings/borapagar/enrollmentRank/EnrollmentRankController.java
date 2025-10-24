package com.borathings.borapagar.enrollmentRank;

import com.borathings.borapagar.enrollmentRank.dto.EnrollmentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/enrollments")
public interface EnrollmentRankController {

    @GetMapping("/me")
    public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentRanksByStudent(Authentication authentication);
}
