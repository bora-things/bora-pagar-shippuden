package com.borathings.borapagar.student;

import com.borathings.borapagar.student.dto.StudentClassResponseDTO;
import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.student.dto.StudentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {

    public StudentDTO toDto(StudentEntity entity);

    public StudentEntity toEntity(StudentDTO dto);

    @Mapping(target = "period", expression = "java(entity.getUserPeriod())")
    public StudentResponseDTO toResponseDTO(StudentEntity entity);

    public StudentClassResponseDTO toClassResponse(StudentDTO entity);
}
