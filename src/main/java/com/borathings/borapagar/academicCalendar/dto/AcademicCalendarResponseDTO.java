package com.borathings.borapagar.academicCalendar.dto;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

public record AcademicCalendarResponseDTO(
        boolean isCurrent,
        Integer unitId,
        int year,
        int period,
        Instant vacationPeriodStart,
        Instant vacationPeriodEnd,
        Instant periodStart,
        Instant periodEnd,
        Instant onlineEnrollmentStart,
        Instant onlineEnrollmentEnd,
        Instant enrollmentProcessingStart,
        Instant enrollmentProcessingEnd,
        Instant reEnrollmentStart,
        Instant reEnrollmentEnd,
        Instant reEnrollmentProcessingStart,
        Instant reEnrollmentProcessingEnd,
        Instant extraordinaryEnrollmentStart,
        Instant extraordinaryEnrollmentEnd,
        Instant vacationClassEnrollmentStart,
        Instant vacationClassEnrollmentEnd,
        Instant extraordinaryVacationEnrollmentStart,
        Instant extraordinaryVacationEnrollmentEnd) {}
