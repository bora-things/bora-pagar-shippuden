package com.borathings.borapagar.subject;

import com.borathings.borapagar.subject.dto.CurricularComponentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/subjects")
public interface SubjectController {
    @GetMapping
    public ResponseEntity<CurricularComponentDTO> getSubject(@RequestParam String code);
}
