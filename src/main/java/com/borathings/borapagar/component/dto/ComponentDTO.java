package com.borathings.borapagar.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ComponentDTO(
        @JsonProperty("carga-horaria-total") Integer totalWorkload,
        @JsonProperty("coRequisites") String coRequisites,
        @JsonProperty("codigo") String code,
        @JsonProperty("blockComponents") String blockComponents,
        @JsonProperty("departamento") String department,
        @JsonProperty("tipo-atividade-descricao") String activityTypeDescription,
        @JsonProperty("obrigatoria") Boolean mandatorySubject,
        @JsonProperty("ementa") String description,
        @JsonProperty("equivalente") String equivalent,
        @JsonProperty("id-componente") Integer componentId,
        @JsonProperty("id-matriz-curricular") Integer curricularMatrixId,
        @JsonProperty("id-tipo-atividade") Integer activityTypeId,
        @JsonProperty("id-tipo-componente") Integer componentTypeId,
        @JsonProperty("id-unidade") Integer unitId) {}
