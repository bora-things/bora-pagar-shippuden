package com.borathings.borapagar.student.interest.dto;

import com.borathings.borapagar.component.dto.ComponentDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentSubjectInterestDTO(
        @JsonProperty("interest_id") Long interestId,
        @JsonProperty("component") ComponentDTO component,
        @JsonProperty("year") int year,
        @JsonProperty("period") int period) {}
