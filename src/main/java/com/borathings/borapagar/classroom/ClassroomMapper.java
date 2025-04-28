package com.borathings.borapagar.classroom;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.borathings.borapagar.student.dto.StudentClassResponseDTO;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClassroomMapper {

     ClassroomEntity toEntity(ClassroomDTO classroomDTO);

    ClassroomDTO toDTO(ClassroomEntity classroomEntity);

    @Mapping(source="component",target="component")
    @Mapping(expression="java(classroomEntity.getPeriod())",target="period")
    ClassroomResponseDTO toResponseDTO(ClassroomEntity classroomEntity, ComponentResponseDTO component, List<UserResponseDTO> friends);
}
