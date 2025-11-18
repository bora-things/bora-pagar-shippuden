package com.borathings.borapagar.academicCalendar;

import com.borathings.borapagar.academicCalendar.dto.AcademicCalendarDTO;
import com.borathings.borapagar.academicCalendar.dto.AcademicCalendarResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AcademicCalendarMapper {

    public AcademicCalendarEntity toEntity(AcademicCalendarDTO dto);

    public AcademicCalendarResponseDTO toResponseDTO(AcademicCalendarEntity entity);
}
