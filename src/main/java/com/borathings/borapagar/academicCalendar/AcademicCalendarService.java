package com.borathings.borapagar.academicCalendar;

import com.borathings.borapagar.academicCalendar.dto.AcademicCalendarDTO;
import com.borathings.borapagar.academicCalendar.dto.AcademicCalendarResponseDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AcademicCalendarService {

    private final AcademicCalendarMapper mapper;

    private final AcademicCalendarRepository repository;

    @Qualifier("serviceRestClient")
    private final RestClient serviceRestClient;

    @Async
    public void fetchCalendar() {

        List<AcademicCalendarDTO> calendars = serviceRestClient
                .get()
                .uri("/calendario/v1/calendarios?ano=2025&sigla-nivel-ensino=G")
                .retrieve()
                .body(new ParameterizedTypeReference<List<AcademicCalendarDTO>>() {});

        if (calendars != null && calendars.size() > 0) {
            List<AcademicCalendarEntity> calendarEntities =
                    calendars.stream().map(item -> mapper.toEntity(item)).collect(Collectors.toList());
            repository.deleteAll();
            repository.saveAll(calendarEntities);
        }
    }

    public List<AcademicCalendarResponseDTO> getCalendar() {

        List<AcademicCalendarEntity> calendars = repository.findAll();
        return calendars.stream()
                .map(calendar -> mapper.toResponseDTO(calendar))
                .collect(Collectors.toList());
    }
}
