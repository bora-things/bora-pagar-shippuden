package com.borathings.borapagar.docent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public record DocentResponseDTO(
        String name,
        Double review,
        String imageUrl
) {
}
