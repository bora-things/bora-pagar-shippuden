package com.borathings.borapagar.student.interest.dto;

import com.borathings.borapagar.component.dto.ComponentDTO;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record StudentSubjectInterestDTO(
        @JsonProperty("interest_id") Long interestId,
        @JsonProperty("component") ComponentDTO component,
        @JsonProperty("year") int year,
        @JsonProperty("period") int period,
        @JsonProperty("friends") List<UserResponseDTO> friends)
{}
