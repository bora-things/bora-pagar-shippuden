package com.borathings.borapagar.academicCalendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat; // <-- ADICIONE ESTE IMPORT
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record AcademicCalendarDTO(
        @JsonProperty("id-calendario") Long calendarId,
        @JsonProperty("vigente") boolean isCurrent,
        @JsonProperty("id-unidade") Integer unitId,
        @JsonProperty("ano") int year,
        @JsonProperty("periodo") int period,
        @JsonProperty("ano-ferias-vigente") Integer currentVacationYear,
        @JsonProperty("periodo-ferias-vigente") Integer currentVacationPeriod,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("inicio-periodo-ferias") Instant vacationPeriodStart,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("fim-periodo-ferias") Instant vacationPeriodEnd,
        @JsonProperty("sigla-nivel-ensino") String educationLevelAcronym,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("inicio-periodo") Instant periodStart,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("fim-periodo") Instant periodEnd,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("inicio-matricula-online")
                Instant onlineEnrollmentStart,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("fim-matricula-online") Instant onlineEnrollmentEnd,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("inicio-processamento-matricula")
                Instant enrollmentProcessingStart,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("fim-processamento-matricula")
                Instant enrollmentProcessingEnd,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("inicio-rematricula") Instant reEnrollmentStart,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("fim-rematricula") Instant reEnrollmentEnd,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("inicio-processamento-rematricula")
                Instant reEnrollmentProcessingStart,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("fim-processamento-rematricula")
                Instant reEnrollmentProcessingEnd,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("inicio-matricula-extraordinaria")
                Instant extraordinaryEnrollmentStart,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("fim-matricula-extraordinaria")
                Instant extraordinaryEnrollmentEnd,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("inicio-matricula-turma-ferias")
                Instant vacationClassEnrollmentStart, // Can be null
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("fim-matricula-turma-ferias")
                Instant vacationClassEnrollmentEnd, // Can be null
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("inicio-matricula-extraordinaria-ferias")
                Instant extraordinaryVacationEnrollmentStart,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER) @JsonProperty("fim-matricula-extraordinaria-ferias")
                Instant extraordinaryVacationEnrollmentEnd) {}
