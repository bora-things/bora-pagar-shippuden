package com.borathings.borapagar.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ComponentResponseDTO(
        @JsonProperty("codigo")
        String code,

        @JsonProperty("nome")
        String name,

        @JsonProperty("disciplina-obrigatoria")
        Boolean mandatoryDiscipline,

        @JsonProperty("departamento")
        String department,

        @JsonProperty("carga-horaria-total")
        Integer totalWorkload,

        @JsonProperty("pre-requisitos")
        String prerequisites,

        @JsonProperty("co-requisitos")
        String corequisites,

        @JsonProperty("equivalentes")
        String equivalents,

        @JsonProperty("nivel")
        String level,

        @JsonProperty("descricao-tipo-atividade")
        String activityTypeDescription

) {
}
