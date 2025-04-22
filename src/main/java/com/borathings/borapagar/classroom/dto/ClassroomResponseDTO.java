package com.borathings.borapagar.classroom.dto;

import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ClassroomResponseDTO(
        @JsonProperty("ano") int year,
        @JsonProperty("capacidade-aluno") int studentCapacity,
        @JsonProperty("codigo-turma") String classroomCode,
        @JsonProperty("descricao-horario") String scheduleDescription,
        @JsonProperty("id-docente") long teacherId,
        @JsonProperty("id-situacao-turma") long classroomStatusId,
        @JsonProperty("id-turma") long classroomId,
        @JsonProperty("id-unidade") long unitId,
        @JsonProperty("local") String location,
        @JsonProperty("periodo") int period,
        @JsonProperty("sigla-nivel") String levelAbbreviation,
        @JsonProperty("component") ComponentResponseDTO component
        ) {
}
