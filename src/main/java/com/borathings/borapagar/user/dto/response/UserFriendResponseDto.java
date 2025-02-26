package com.borathings.borapagar.user.dto.response;

public record UserFriendResponseDto(
        String personName,
        String courseName,
        Integer period,
        String imageUrl
) {
}
