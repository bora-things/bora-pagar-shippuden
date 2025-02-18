package com.borathings.borapagar.user;

import com.borathings.borapagar.user.dto.UserDTO;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/** UserMapper */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    public UserResponseDTO toUserResponseDTO(UserEntity userEntity);

    public UserDTO toDto(UserEntity entity);

    public UserEntity toEntity(UserDTO dto);
}
