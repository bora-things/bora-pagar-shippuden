package com.borathings.borapagar.task;

import com.borathings.borapagar.academicCalendar.AcademicCalendarService;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.enrollmentRank.EnrollmentRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {


    private final ComponentService componentService;

    private final AcademicCalendarService calendarService;
    private final EnrollmentRankService enrollmentRankService;

    @Override
    public void run(ApplicationArguments args) {
        componentService.fetchComponents();
        calendarService.fetchCalendar();
//        enrollmentRankService.processAndSaveEnrollmentRanks();

    }
}
