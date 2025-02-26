package com.borathings.borapagar.subject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CurricularComponentDTO(
        @JsonProperty("carga-horaria-total") Integer totalWorkload,
        @JsonProperty("co-requisitos") String corequisites,
        @JsonProperty("codigo") String code,
        @JsonProperty("componentesBloco") List<Object> blockComponents,
        @JsonProperty("departamento") String department,
        @JsonProperty("descricao-tipo-atividade") String activityTypeDescription,
        @JsonProperty("disciplina-obrigatoria") Boolean mandatoryCourse,
        @JsonProperty("ementa") String syllabus,
        @JsonProperty("equivalentes") String equivalents,
        @JsonProperty("id-componente") Long componentId,
        @JsonProperty("id-matriz-curricular") Integer curriculumMatrixId,
        @JsonProperty("id-tipo-atividade") Long activityTypeId,
        @JsonProperty("id-tipo-componente") Long componentTypeId,
        @JsonProperty("id-unidade") Long unitId,
        @JsonProperty("nivel") String level,
        @JsonProperty("nome") String name,
        @JsonProperty("num-unidades") Integer numberOfUnits,
        @JsonProperty("pre-requisitos") String prerequisites,
        @JsonProperty("semestre-oferta") Integer offerSemester) {}
