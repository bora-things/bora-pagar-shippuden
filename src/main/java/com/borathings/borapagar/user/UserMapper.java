package com.borathings.borapagar.user;

import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.user.dto.UserDTO;
import com.borathings.borapagar.user.dto.response.UserFriendResponseDto;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/** UserMapper */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "name", expression = "java(userEntity.getPersonName())")
    public UserResponseDTO toUserResponseDTO(UserEntity userEntity);

    public UserDTO toDto(UserEntity entity);

    public UserEntity toEntity(UserDTO dto);

    @Mapping(target = "period", expression = "java(student.getUserPeriod())")
    @Mapping(target = "imageUrl", expression = "java(student.getImageUrl())")
    public UserFriendResponseDto toUserFriendResponseDto(UserEntity user, StudentEntity student);
}
