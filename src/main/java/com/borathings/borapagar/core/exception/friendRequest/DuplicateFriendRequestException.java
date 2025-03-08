package com.borathings.borapagar.core.exception.friendRequest;

public class DuplicateFriendRequestException extends RuntimeException {
    public DuplicateFriendRequestException() {
        super("Pedido de amizade já existente");
    }
}
