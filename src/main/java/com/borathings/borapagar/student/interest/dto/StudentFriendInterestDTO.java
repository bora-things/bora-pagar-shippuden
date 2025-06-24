package com.borathings.borapagar.student.interest.dto;

import java.util.List;

public record StudentFriendInterestDTO(
        String name,
        String imageUrl,
        List<String> interestsCodes
) {
}
