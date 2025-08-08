package com.borathings.borapagar.user.exception;

import com.borathings.borapagar.core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UsersNotFriendsException extends ApiException {
    public UsersNotFriendsException() {
        super(HttpStatus.BAD_REQUEST, "Usuários não são amigos");
    }
}
