package com.borathings.borapagar.friendRequest.exception;

import com.borathings.borapagar.core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DuplicateFriendRequestException extends ApiException {
    public DuplicateFriendRequestException() {
        super(HttpStatus.BAD_REQUEST, "Pedido de amizade já existente");
    }
}
