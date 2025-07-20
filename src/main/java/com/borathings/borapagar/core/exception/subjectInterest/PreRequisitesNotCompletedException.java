package com.borathings.borapagar.core.exception.subjectInterest;

import com.borathings.borapagar.core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class PreRequisitesNotCompletedException extends ApiException {

    public PreRequisitesNotCompletedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
