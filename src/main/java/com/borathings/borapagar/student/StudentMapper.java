package com.borathings.borapagar.student;

import com.borathings.borapagar.student.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/** StudentMapper */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {

    public StudentDTO toDto(StudentEntity entity);

    public StudentEntity toEntity(StudentDTO dto);
}
