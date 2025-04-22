package com.borathings.borapagar.classroom;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClassroomMapper {

     ClassroomEntity toEntity(ClassroomDTO classroomDTO);

    ClassroomDTO toDTO(ClassroomEntity classroomEntity);

    @Mapping(source = "classroomEntity.unitId", target = "unitId")
    @Mapping(source="component",target="component")
    ClassroomResponseDTO toResponseDTO(ClassroomEntity classroomEntity, ComponentResponseDTO component);
}
