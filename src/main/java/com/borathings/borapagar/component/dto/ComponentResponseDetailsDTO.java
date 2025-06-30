package com.borathings.borapagar.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ComponentResponseDetailsDTO(
        @JsonProperty("carga-horaria-total") Integer totalWorkload,
        @JsonProperty("co-requisites") String coRequisites,
        @JsonProperty("pre-requisites") String preRequisites,
        @JsonProperty("codigo") String code,
        @JsonProperty("departamento") String department,
        @JsonProperty("disciplina-obrigatoria") Boolean mandatorySubject,
        @JsonProperty("nome") String name,
        @JsonProperty("equivalentes") String equivalent,
        @JsonProperty("id-componente") Integer componentId,
        @JsonProperty("id-matriz-curricular") Integer curricularMatrixId,
        @JsonProperty("id-unidade") Integer unitId,
        @JsonProperty("ementa") String objectives,
        @JsonProperty("conteudo") String content
        )
{}
