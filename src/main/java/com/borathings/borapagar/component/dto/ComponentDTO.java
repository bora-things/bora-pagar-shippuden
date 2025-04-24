package com.borathings.borapagar.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ComponentDTO(

        @JsonProperty("id-componente")
        Long componentId,

        @JsonProperty("codigo")
        String code,

        @JsonProperty("nome")
        String name,

        @JsonProperty("disciplina-obrigatoria")
        Boolean mandatorySubject,

        @JsonProperty("semestre-oferta")
        Integer offeredSemester,

        @JsonProperty("id-tipo-componente")
        Long componentTypeId,

        @JsonProperty("id-matriz-curricular")
        Long curricularMatrixId,

        @JsonProperty("id-unidade")
        Long unitId,

        @JsonProperty("departamento")
        String department,

        @JsonProperty("carga-horaria-total")
        Integer totalWorkload,

        @JsonProperty("pre-requisitos")
        String preRequisites,

        @JsonProperty("co-requisitos")
        String coRequisites,

        @JsonProperty("equivalentes")
        String equivalent,

        @JsonProperty("nivel")
        String level,

        @JsonProperty("id-tipo-atividade")
        Long activityTypeId,

        @JsonProperty("descricao-tipo-atividade")
        String activityTypeDescription,

        @JsonProperty("num-unidades")
        Integer numberOfUnits

) {}

