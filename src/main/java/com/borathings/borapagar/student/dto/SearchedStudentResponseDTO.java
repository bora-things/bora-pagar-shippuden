package com.borathings.borapagar.student.dto;

import com.borathings.borapagar.student.enums.FriendStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SearchedStudentResponseDTO(
        long id,
        String studentName,
        String courseName,
        String imageUrl,
        Integer friends,
        FriendStatus friendStatus,
        Long requestId,
        Integer period) {
}
