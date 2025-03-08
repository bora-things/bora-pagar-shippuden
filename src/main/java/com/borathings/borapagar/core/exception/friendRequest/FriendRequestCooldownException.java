package com.borathings.borapagar.core.exception.friendRequest;

public class FriendRequestCooldownException extends RuntimeException {
    public FriendRequestCooldownException() {
        super("Friend request has cooldown");
    }
}
