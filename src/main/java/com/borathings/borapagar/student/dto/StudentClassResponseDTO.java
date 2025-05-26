package com.borathings.borapagar.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentClassResponseDTO(
        @JsonProperty("id") long id,
        @JsonProperty("name") String studentName,
        @JsonProperty("course_name") String courseName,
        @JsonProperty("login") String login,
        @JsonProperty("image_url") String imageUrl,
        @JsonProperty("period") Integer period) {}
