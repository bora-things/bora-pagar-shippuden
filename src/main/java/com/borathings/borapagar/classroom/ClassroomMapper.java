package com.borathings.borapagar.classroom;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClassroomMapper {

     ClassroomEntity toEntity(ClassroomDTO classroomDTO);

    ClassroomDTO toDTO(ClassroomEntity classroomEntity);
}
