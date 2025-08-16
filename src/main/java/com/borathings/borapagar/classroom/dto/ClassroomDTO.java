package com.borathings.borapagar.classroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClassroomDTO(
        @JsonProperty("codigo-componente") String componentCode,
        @JsonProperty("codigo-turma") String classroomCode,
        @JsonProperty("id-docente") long teacherId,
        @JsonProperty("id-situacao-turma") long classroomStatusId,
        @JsonProperty("id-turma") long classroomId,
        @JsonProperty("id-unidade") long unitId,
        @JsonProperty("nome-componente") String componentName,
        @JsonProperty("ano") int year,
        @JsonProperty("periodo") int period) {}
