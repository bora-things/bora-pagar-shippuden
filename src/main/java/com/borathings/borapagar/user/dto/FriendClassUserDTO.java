package com.borathings.borapagar.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendClassUserDTO(
        @JsonProperty("id-institucional") Long institutionalId
) {
}
