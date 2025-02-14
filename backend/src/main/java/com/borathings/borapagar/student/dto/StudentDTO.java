package com.borathings.borapagar.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentDTO(
        @JsonProperty("id-discente") Long studentId,
        @JsonProperty("ano-ingresso") Integer admissionYear,
        @JsonProperty("cpf-cnpj") Long cpf,
        @JsonProperty("descricao-forma-ingresso") String ingressMethodDescription,
        @JsonProperty("email") String email,
        @JsonProperty("id-curso") Long courseId,
        @JsonProperty("id-forma-ingresso") Long enrollmentId,
        @JsonProperty("id-foto") Integer photoId,
        @JsonProperty("id-gestora-academica") Long academicManagerId,
        @JsonProperty("id-institucional") Long institutionalId,
        @JsonProperty("id-instituicao-ensino") Integer educationalInstitutionId,
        @JsonProperty("id-polo") Integer campusId,
        @JsonProperty("id-situacao-discente") Integer studentStatusId,
        @JsonProperty("id-tipo-discente") Integer studentTypeId,
        @JsonProperty("id-tipo-participante") Integer participantTypeId,
        @JsonProperty("id-unidade") Long unitId,
        @JsonProperty("instituicao-ensino") String educationalInstitution,
        @JsonProperty("login") String login,
        @JsonProperty("matricula") Long registrationNumber,
        @JsonProperty("nome-curso") String courseName,
        @JsonProperty("nome-discente") String studentName,
        @JsonProperty("periodo-ingresso") Integer admissionPeriod,
        @JsonProperty("polo") String campus,
        @JsonProperty("sigla-nivel") String level) {}
