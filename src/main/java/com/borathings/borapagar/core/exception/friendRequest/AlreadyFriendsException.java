package com.borathings.borapagar.core.exception.friendRequest;

public class AlreadyFriendsException extends RuntimeException {
    public AlreadyFriendsException() {
        super("Usuários já são amigos");
    }
}
