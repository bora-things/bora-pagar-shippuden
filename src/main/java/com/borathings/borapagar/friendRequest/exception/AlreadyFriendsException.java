package com.borathings.borapagar.friendRequest.exception;

import com.borathings.borapagar.core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AlreadyFriendsException extends ApiException {
    public AlreadyFriendsException() {
        super(HttpStatus.BAD_REQUEST,"Usuários já são amigos");
    }
}
