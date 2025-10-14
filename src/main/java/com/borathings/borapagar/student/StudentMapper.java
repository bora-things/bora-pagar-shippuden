package com.borathings.borapagar.student;

import com.borathings.borapagar.student.dto.SearchedStudentResponseDTO;
import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.student.dto.StudentResponseDTO;
import com.borathings.borapagar.student.enums.FriendStatus;
import com.borathings.borapagar.student.enums.StudentSituation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {

    public StudentDTO toDto(StudentEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target="curricularMatrix",expression = "java(curricularMatrix)")
    public StudentEntity toEntity(StudentDTO dto,Integer curricularMatrix, StudentSituation studentSituation);

    @Mapping(target = "period", expression = "java(entity.getUserPeriod())")
    public StudentResponseDTO toResponseDTO(StudentEntity entity);

    @Mapping(target = "period", expression = "java(entity.getUserPeriod())")
    @Mapping(target = "friends", expression = "java(entity.getUser().getFriends().size())")
    public SearchedStudentResponseDTO toSearchedResponseDTO(
            StudentEntity entity, FriendStatus friendStatus, Long requestId);
}
