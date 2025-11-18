package com.borathings.borapagar.academicCalendar;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendarEntity, Long> {

    void deleteAllByYear(int year);

    AcademicCalendarEntity findByCurrentTrue();
}
