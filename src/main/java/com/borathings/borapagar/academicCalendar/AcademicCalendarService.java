package com.borathings.borapagar.academicCalendar;

import com.borathings.borapagar.academicCalendar.dto.AcademicCalendarDTO;
import com.borathings.borapagar.academicCalendar.dto.AcademicCalendarResponseDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AcademicCalendarService {

    private final AcademicCalendarMapper mapper;

    private final AcademicCalendarRepository repository;

    @Qualifier("serviceRestClient")
    private final RestClient serviceRestClient;

    @Async
    @Transactional
    public void fetchCalendar() {

        Integer year= LocalDate.now().getYear();
        List<AcademicCalendarDTO> calendars = serviceRestClient
                .get()
                .uri("/calendario/v1/calendarios?ano="+year+"&sigla-nivel-ensino=G")
                .retrieve()
                .body(new ParameterizedTypeReference<List<AcademicCalendarDTO>>() {});

        if (calendars != null && calendars.size() > 0) {
            List<AcademicCalendarEntity> calendarEntities =
                    calendars.stream().map(item -> mapper.toEntity(item)).collect(Collectors.toList());
            repository.deleteAllByYear(year);
            repository.saveAll(calendarEntities);
        }
    }

    public List<AcademicCalendarResponseDTO> getCalendar() {
        List<AcademicCalendarEntity> calendars = repository.findAll();
        return calendars.stream()
                .map(calendar -> mapper.toResponseDTO(calendar))
                .collect(Collectors.toList());
    }

    public AcademicCalendarResponseDTO getCurrentCalendar(){
        AcademicCalendarEntity calendar=repository.findByIsCurrentTrue();
        return mapper.toResponseDTO(calendar);
    }
}
