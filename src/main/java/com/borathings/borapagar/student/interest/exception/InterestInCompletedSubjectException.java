package com.borathings.borapagar.student.interest.exception;

import com.borathings.borapagar.core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InterestInCompletedSubjectException extends ApiException {
    public InterestInCompletedSubjectException() {
        super(HttpStatus.BAD_REQUEST, "Matéria já presente no currículo");
    }
}
