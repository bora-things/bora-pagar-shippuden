package com.borathings.borapagar.core.exception;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/** Essa classe representa um erro de validação Ela contém um map de erros dos campos que falharam na validação */
@Setter
@Getter
public class ApiFieldException extends ApiException {
    public ApiFieldException(HttpStatus status, Map<String, List<String>> fieldErrors) {
        super(status);
        this.setMessage(
                "Um ou mais campos falharam na validação. Verifique o atributo fieldErrors para" + " mais detalhes.");
        this.fieldErrors = fieldErrors;
    }

    Map<String, List<String>> fieldErrors;
}
