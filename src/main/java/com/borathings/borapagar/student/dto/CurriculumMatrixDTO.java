package com.borathings.borapagar.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurriculumMatrixDTO(
        @JsonProperty("ano") int year,
        @JsonProperty("ativo") boolean active,
        @JsonProperty("ch-complementar-minima") int minimumComplementaryCreditHours,
        @JsonProperty("ch-optativas-minima") int minimumElectiveCreditHours,
        @JsonProperty("ch-total-minima") int minimumTotalCreditHours,
        @JsonProperty("codigo-curriculo") String curriculumCode,
        @JsonProperty("creditos-ideal-semestre") int idealCreditsPerSemester,
        @JsonProperty("creditos-maximo-semestre") int maximumCreditsPerSemester,
        @JsonProperty("creditos-minimo-semestre") int minimumCreditsPerSemester,
        @JsonProperty("enfase") String emphasis,
        @JsonProperty("id-curso") int courseId,
        @JsonProperty("id-grau-academico") int academicDegreeId,
        @JsonProperty("id-matriz-curricular") int curriculumMatrixId,
        @JsonProperty("id-modalidade-educacao") int educationModalityId,
        @JsonProperty("municipio") String city,
        @JsonProperty("nome-curso") String courseName,
        @JsonProperty("periodo") int period,
        @JsonProperty("semestre-conclusao-ideal") int idealCompletionSemester,
        @JsonProperty("semestre-conclusao-maximo") int maximumCompletionSemester,
        @JsonProperty("semestre-conclusao-minimo") int minimumCompletionSemester,
        @JsonProperty("turno") String shift) {}
