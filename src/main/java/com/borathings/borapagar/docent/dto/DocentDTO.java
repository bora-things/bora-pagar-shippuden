package com.borathings.borapagar.docent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DocentDTO(
       @JsonProperty("id-docente")
        Long teacherId
) {
}
