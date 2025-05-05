package com.borathings.borapagar.classroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClassroomDTO(
        @JsonProperty("ano") int year,
        @JsonProperty("capacidade-aluno") int studentCapacity,
        @JsonProperty("codigo-componente") String componentCode,
        @JsonProperty("codigo-turma") String classroomCode,
        @JsonProperty("descricao-horario") String scheduleDescription,
        @JsonProperty("id-componente") long componentId,
        @JsonProperty("id-discente") long studentId,
        @JsonProperty("id-docente") long teacherId,
        @JsonProperty("id-docente-externo") long externalTeacherId,
        @JsonProperty("id-modalidade-educacao") long educationModeId,
        @JsonProperty("id-situacao-turma") long classroomStatusId,
        @JsonProperty("id-turma") long classroomId,
        @JsonProperty("id-turma-agrupadora") long groupingClassroomId,
        @JsonProperty("id-unidade") long unitId,
        @JsonProperty("local") String location,
        @JsonProperty("nome-componente") String componentName,
        @JsonProperty("periodo") int period,
        @JsonProperty("sigla-nivel") String levelAbbreviation,
        @JsonProperty("subturma") boolean isSubClassroom,
        @JsonProperty("tipo") int type,
        @JsonProperty("utiliza-nova-turma-virtual") boolean usesNewVirtualClassroom) {}
