package com.borathings.borapagar.friendRequest.exception;

import com.borathings.borapagar.core.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FriendRequestCooldownException extends ApiException {
    public FriendRequestCooldownException() {
        super(HttpStatus.BAD_REQUEST, "Pedido de amizade em cooldown");
    }
}
