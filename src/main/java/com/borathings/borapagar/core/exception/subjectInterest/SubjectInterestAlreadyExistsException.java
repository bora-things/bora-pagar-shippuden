package com.borathings.borapagar.core.exception.subjectInterest;

import com.borathings.borapagar.core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SubjectInterestAlreadyExistsException extends ApiException {

    public SubjectInterestAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "Usuário já possui interesse nessa matéria");
    }
}
