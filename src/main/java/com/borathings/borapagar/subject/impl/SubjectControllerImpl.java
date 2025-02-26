package com.borathings.borapagar.subject.impl;

import com.borathings.borapagar.subject.SubjectController;
import com.borathings.borapagar.subject.SubjectService;
import com.borathings.borapagar.subject.dto.CurricularComponentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubjectControllerImpl implements SubjectController {

    @Autowired
    SubjectService subjectService;

    @Override
    public ResponseEntity<CurricularComponentDTO> getSubject(String code) {
        return ResponseEntity.ok(subjectService.getSubjectByCode(code));
    }
}
