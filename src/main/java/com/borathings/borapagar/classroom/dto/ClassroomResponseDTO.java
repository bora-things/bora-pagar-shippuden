package com.borathings.borapagar.classroom.dto;

import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ClassroomResponseDTO(
        @JsonProperty("ano") int year,
        @JsonProperty("id-turma") long classroomId,
        @JsonProperty("periodo") int period,
        @JsonProperty("component") ComponentResponseDTO component,
        List<UserResponseDTO> friends
        ) {
}
