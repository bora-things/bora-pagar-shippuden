package com.borathings.borapagar.student.transcript.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TranscriptComponentDTO(
        @JsonProperty("ano") int year,
        @JsonProperty("periodo") int period,
        @JsonProperty("data-cadastro") long registerDate,
        @JsonProperty("faltas") int absences,
        @JsonProperty("id-componente") int componentId,
        @JsonProperty("id-discente") int studentId,
        @JsonProperty("id-matricula-componente") int registrationId,
        @JsonProperty("id-situacao-matricula") int registrationSituationId,
        @JsonProperty("id-tipo-integralizacao") String integralizationKindId,
        @JsonProperty("id-turma") int sigaaClassId) {}
