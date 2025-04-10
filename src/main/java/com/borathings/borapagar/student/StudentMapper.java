package com.borathings.borapagar.student;

import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.student.dto.StudentInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/** StudentMapper */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {

    public StudentDTO toDto(StudentEntity entity);

    public StudentEntity toEntity(StudentDTO dto);

    @Mapping(target = "period", expression = "java(entity.getUserPeriod())")
    public StudentInfoDto toStudentInfoDto(StudentEntity entity);

}
