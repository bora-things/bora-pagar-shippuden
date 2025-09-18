package com.borathings.borapagar.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class WhiteListException extends AuthenticationException {

    public WhiteListException() {
        super("Usuário não tem permissão para acessar a aplicação");
    }
}
