package com.borathings.borapagar.friendRequest;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestStatusConverter implements Converter<String, FriendRequestStatus> {

    @Override
    public FriendRequestStatus convert(String source) {
        return FriendRequestStatus.valueOf(source.toUpperCase());
    }
}
