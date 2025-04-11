package com.borathings.borapagar.student;

import com.borathings.borapagar.student.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/** StudentMapper */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {

    @Mapping(target = "period", expression = "java(entity.getUserPeriod())")
    public StudentDTO toDto(StudentEntity entity);

    public StudentEntity toEntity(StudentDTO dto);
}
