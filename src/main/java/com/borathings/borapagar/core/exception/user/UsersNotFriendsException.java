package com.borathings.borapagar.core.exception.user;

public class UsersNotFriendsException extends RuntimeException {
    public UsersNotFriendsException() {
        super("Usuários não são amigos");
    }
}
