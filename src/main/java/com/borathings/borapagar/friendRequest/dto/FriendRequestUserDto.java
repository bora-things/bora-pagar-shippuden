package com.borathings.borapagar.friendRequest.dto;

public record FriendRequestUserDto(
        String personName,
        String imageUrl,
        String courseName,
        Integer admissionYear
) {
}
