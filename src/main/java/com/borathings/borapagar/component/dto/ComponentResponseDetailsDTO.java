package com.borathings.borapagar.component.dto;

import com.borathings.borapagar.docent.dto.DocentResponseDTO;
import com.borathings.borapagar.user.dto.response.UserFriendResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ComponentResponseDetailsDTO(
        @JsonProperty("workload") Integer totalWorkload,
        @JsonProperty("co-requisites") String coRequisites,
        @JsonProperty("pre-requisites") String preRequisites,
        @JsonProperty("code") String code,
        @JsonProperty("mandatory") Boolean mandatorySubject,
        @JsonProperty("name") String name,
        @JsonProperty("equivalents") String equivalent,
        @JsonProperty("componentId") Integer componentId,
        @JsonProperty("description") String objectives,
        List<UserFriendResponseDto> friends,
        List<DocentResponseDTO> teachers) {}
