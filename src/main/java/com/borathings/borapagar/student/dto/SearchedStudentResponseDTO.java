package com.borathings.borapagar.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SearchedStudentResponseDTO(
        @JsonProperty("id") long id,
        @JsonProperty("name") String studentName,
        @JsonProperty("course_name") String courseName,
        @JsonProperty("image_url") String imageUrl,
        @JsonProperty("isOwner") boolean isOwner,
        @JsonProperty("isFriend") boolean isFriend,
        Integer friends,
        @JsonProperty("period") Integer period) {}
