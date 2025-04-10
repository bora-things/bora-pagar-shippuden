package com.borathings.borapagar.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentInfoDto(
        @JsonProperty("name") String studentName,
        @JsonProperty("course-name") String courseName,
        @JsonProperty("login") String login,
        @JsonProperty("image-url") String imageUrl,
        @JsonProperty("period") Integer period


) {}
