package com.borathings.borapagar.academicCalendar;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.*;

@Entity
@Table(name = "academic_calendar")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class AcademicCalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long calendarId;
    private boolean current;
    private Integer unitId;
    private int year;
    private int period;
    private Integer currentVacationYear;
    private Integer currentVacationPeriod;
    private Instant vacationPeriodStart;
    private Instant vacationPeriodEnd;
    private Instant periodStart;
    private Instant periodEnd;
    private Instant onlineEnrollmentStart;
    private Instant onlineEnrollmentEnd;
    private Instant enrollmentProcessingStart;
    private Instant enrollmentProcessingEnd;
    private Instant reEnrollmentStart;
    private Instant reEnrollmentEnd;
    private Instant reEnrollmentProcessingStart;
    private Instant reEnrollmentProcessingEnd;
    private Instant extraordinaryEnrollmentStart;
    private Instant extraordinaryEnrollmentEnd;
    private Instant vacationClassEnrollmentStart;
    private Instant vacationClassEnrollmentEnd;
    private Instant extraordinaryVacationEnrollmentStart;
    private Instant extraordinaryVacationEnrollmentEnd;
}
