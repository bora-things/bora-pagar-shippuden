package com.borathings.borapagar.student.interest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentSubjectAddInterestDTO(
        @JsonProperty("subjectCode") String subjectCode,
        @JsonProperty("year") int year,
        @JsonProperty("period") int period) {}
