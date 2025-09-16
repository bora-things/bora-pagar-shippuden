package com.borathings.borapagar.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ComponentResponseDTO(
        @JsonProperty("codigo") String code,
        @JsonProperty("nome") String name,
        @JsonProperty("disciplina-obrigatoria") Boolean mandatorySubject,
        @JsonProperty("carga-horaria-total") Integer totalWorkload) {}
