package com.borathings.borapagar.academicCalendar.dto;

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
        @JsonProperty("inicio-periodo-ferias") Instant vacationPeriodStart,
        @JsonProperty("fim-periodo-ferias") Instant vacationPeriodEnd,
        @JsonProperty("sigla-nivel-ensino") String educationLevelAcronym,
        @JsonProperty("inicio-periodo") Instant periodStart,
        @JsonProperty("fim-periodo") Instant periodEnd,
        @JsonProperty("inicio-matricula-online") Instant onlineEnrollmentStart,
        @JsonProperty("fim-matricula-online") Instant onlineEnrollmentEnd,
        @JsonProperty("inicio-processamento-matricula") Instant enrollmentProcessingStart,
        @JsonProperty("fim-processamento-matricula") Instant enrollmentProcessingEnd,
        @JsonProperty("inicio-rematricula") Instant reEnrollmentStart,
        @JsonProperty("fim-rematricula") Instant reEnrollmentEnd,
        @JsonProperty("inicio-processamento-rematricula") Instant reEnrollmentProcessingStart,
        @JsonProperty("fim-processamento-rematricula") Instant reEnrollmentProcessingEnd,
        @JsonProperty("inicio-matricula-extraordinaria") Instant extraordinaryEnrollmentStart,
        @JsonProperty("fim-matricula-extraordinaria") Instant extraordinaryEnrollmentEnd,
        @JsonProperty("inicio-matricula-turma-ferias") Instant vacationClassEnrollmentStart, // Can be null
        @JsonProperty("fim-matricula-turma-ferias") Instant vacationClassEnrollmentEnd, // Can be null
        @JsonProperty("inicio-matricula-extraordinaria-ferias") Instant extraordinaryVacationEnrollmentStart,
        @JsonProperty("fim-matricula-extraordinaria-ferias") Instant extraordinaryVacationEnrollmentEnd) {}
