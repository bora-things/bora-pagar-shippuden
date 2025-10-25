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

        @Column(name = "calendar_id")
        private Long calendarId;

        @Column(name = "is_current", nullable = false)
        private boolean isCurrent;

        @Column(name = "unit_id")
        private Integer unitId;

        @Column(name = "year", nullable = false)
        private int year;

        @Column(name = "period", nullable = false)
        private int period;

        @Column(name = "current_vacation_year")
        private Integer currentVacationYear;

        @Column(name = "current_vacation_period")
        private Integer currentVacationPeriod;

        @Column(name = "vacation_period_start")
        private Instant vacationPeriodStart;

        @Column(name = "vacation_period_end")
        private Instant vacationPeriodEnd;

        @Column(name = "education_level_acronym", length = 10)
        private String educationLevelAcronym;

        @Column(name = "period_start")
        private Instant periodStart;

        @Column(name = "period_end")
        private Instant periodEnd;

        @Column(name = "online_enrollment_start")
        private Instant onlineEnrollmentStart;

        @Column(name = "online_enrollment_end")
        private Instant onlineEnrollmentEnd;

        @Column(name = "enrollment_processing_start")
        private Instant enrollmentProcessingStart;

        @Column(name = "enrollment_processing_end")
        private Instant enrollmentProcessingEnd;

        @Column(name = "re_enrollment_start")
        private Instant reEnrollmentStart;

        @Column(name = "re_enrollment_end")
        private Instant reEnrollmentEnd;

        @Column(name = "re_enrollment_processing_start")
        private Instant reEnrollmentProcessingStart;

        @Column(name = "re_enrollment_processing_end")
        private Instant reEnrollmentProcessingEnd;

        @Column(name = "extraordinary_enrollment_start")
        private Instant extraordinaryEnrollmentStart;

        @Column(name = "extraordinary_enrollment_end")
        private Instant extraordinaryEnrollmentEnd;

        @Column(name = "vacation_class_enrollment_start")
        private Instant vacationClassEnrollmentStart;

        @Column(name = "vacation_class_enrollment_end")
        private Instant vacationClassEnrollmentEnd;

        @Column(name = "extraordinary_vacation_enrollment_start")
        private Instant extraordinaryVacationEnrollmentStart;

        @Column(name = "extraordinary_vacation_enrollment_end")
        private Instant extraordinaryVacationEnrollmentEnd;
    }
