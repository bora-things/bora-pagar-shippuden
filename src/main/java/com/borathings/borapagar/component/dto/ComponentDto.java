package com.borathings.borapagar.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ComponentDto(
        @JsonProperty("carga-horaria-total") int totalWorkload,
        @JsonProperty("co-requisitos") String coRequisites,
        @JsonProperty("codigo") String code,
        @JsonProperty("componentesBloco") String blockComponents,
        @JsonProperty("departamento") String department,
        @JsonProperty("descricao-tipo-atividade") String activityTypeDescription,
        @JsonProperty("disciplina-obrigatoria") boolean mandatorySubject,
        @JsonProperty("ementa") String description,
        @JsonProperty("equivalentes") String equivalent,
        @JsonProperty("id-componente") int componentId,
        @JsonProperty("id-matriz-curricular") int curricularMatrixId,
        @JsonProperty("id-tipo-atividade") int activityTypeId,
        @JsonProperty("id-tipo-componente") int componentTypeId,
        @JsonProperty("id-unidade") int unitId,
        @JsonProperty("nivel") String level,
        @JsonProperty("nome") String name,
        @JsonProperty("num-unidades") int numberUnits,
        @JsonProperty("pre-requisitos") String preRequisites,
        @JsonProperty("semestre-oferta") int offeredSemester
) {
}

