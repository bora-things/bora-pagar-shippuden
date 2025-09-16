package com.borathings.borapagar.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ComponentDTO(
        @JsonProperty("carga-horaria-total") Integer totalWorkload,
        @JsonProperty("co-requisitos") String coRequisites,
        @JsonProperty("pre-requisitos") String preRequisites,
        @JsonProperty("codigo") String code,
        @JsonProperty("departamento") String department,
        @JsonProperty("id-matriz-curricular") String curricularMatrixId,
        @JsonProperty("disciplina-obrigatoria") Boolean mandatorySubject,
        @JsonProperty("nome") String name,
        @JsonProperty("ementa") String description,
        @JsonProperty("equivalentes") String equivalent,
        @JsonProperty("id-componente") Integer componentId) {}
