package com.borathings.borapagar.student.interest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentSubjectInterestDTO(
        @JsonProperty("interest_id") int interestId,
        @JsonProperty("year") int year,
        @JsonProperty("period") int period) {}
