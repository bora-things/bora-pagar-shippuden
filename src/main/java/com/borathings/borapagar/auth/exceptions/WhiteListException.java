package com.borathings.borapagar.auth.exceptions;

import com.borathings.borapagar.core.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class WhiteListException extends AuthenticationException {

    public WhiteListException() {
        super("Usuário não tem permissão para acessar a aplicação");
    }

}
