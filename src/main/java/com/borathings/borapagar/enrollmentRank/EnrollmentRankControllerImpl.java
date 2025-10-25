package com.borathings.borapagar.enrollmentRank;

import com.borathings.borapagar.enrollmentRank.dto.EnrollmentResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EnrollmentRankControllerImpl implements EnrollmentRankController {

    private final EnrollmentRankService enrollmentRankService;

    public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentRanksByStudent(Authentication authentication) {
        String userLogin = authentication.getName();
        List<EnrollmentResponseDTO> enrollments = enrollmentRankService.getEnrollmentRanksByStudent(userLogin);
        return ResponseEntity.ok(enrollments);
    }
}
