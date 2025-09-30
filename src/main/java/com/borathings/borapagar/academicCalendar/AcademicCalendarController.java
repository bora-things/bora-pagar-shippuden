package com.borathings.borapagar.academicCalendar;

import com.borathings.borapagar.academicCalendar.dto.AcademicCalendarResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendars")
@RequiredArgsConstructor
public class AcademicCalendarController {

    private final AcademicCalendarService academicCalendarService;

    @GetMapping
    public ResponseEntity<List<AcademicCalendarResponseDTO>> getCalendar() {
        List<AcademicCalendarResponseDTO> calendars = academicCalendarService.getCalendar();
        return ResponseEntity.ok(calendars);
    }
}
