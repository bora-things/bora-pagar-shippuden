package com.borathings.borapagar.student;

import com.borathings.borapagar.student.dto.SearchedStudentResponseDTO;
import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.student.dto.StudentResponseDTO;
import com.borathings.borapagar.student.enums.FriendStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {

    public StudentDTO toDto(StudentEntity entity);

    public StudentEntity toEntity(StudentDTO dto);

    @Mapping(target = "period", expression = "java(entity.getUserPeriod())")
    public StudentResponseDTO toResponseDTO(StudentEntity entity);

    @Mapping(target = "period", expression = "java(entity.getUserPeriod())")
    @Mapping(target = "friends", expression = "java(entity.getUser().getFriends().size())")
    public SearchedStudentResponseDTO toSearchedResponseDTO(
            StudentEntity entity, FriendStatus friendStatus, Long requestId);
}
