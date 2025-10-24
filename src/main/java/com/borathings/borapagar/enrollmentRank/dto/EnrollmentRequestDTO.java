package com.borathings.borapagar.enrollmentRank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EnrollmentRequestDTO(
        @JsonProperty("ano") int year,
        @JsonProperty("codigo-componente") String componentCode,
        @JsonProperty("codigo-turma") String classCode,
        @JsonProperty("id-discente") long studentId,
        @JsonProperty("id-matricula-componente") long enrollmentComponentId,
        @JsonProperty("id-situacao-solicitacao") long requestStatusId,
        @JsonProperty("id-tipo-prioridade") long priorityTypeId,
        @JsonProperty("id-turma") long classId,
        @JsonProperty("nome-componente") String componentName,
        @JsonProperty("periodo") int period,
        @JsonProperty("prioritario") boolean isPriority,
        @JsonProperty("rematricula") boolean isReEnrollment,
        @JsonProperty("reserva") boolean isReservation,
        boolean uncertainRanking
) {

    public EnrollmentRequestDTO(EnrollmentRequestDTO original, boolean uncertainRanking) {
        this(
                original.year(),
                original.componentCode(),
                original.classCode(),
                original.studentId(),
                original.enrollmentComponentId(),
                original.requestStatusId(),
                original.priorityTypeId(),
                original.classId(),
                original.componentName(),
                original.period(),
                original.isPriority(),
                original.isReEnrollment(),
                original.isReservation(),
                uncertainRanking
        );
    }
}