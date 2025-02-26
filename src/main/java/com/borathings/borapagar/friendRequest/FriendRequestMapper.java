package com.borathings.borapagar.friendRequest;

import com.borathings.borapagar.friendRequest.dto.FriendRequestCreateDto;
import com.borathings.borapagar.friendRequest.dto.FriendRequestUserDto;
import com.borathings.borapagar.friendRequest.dto.response.FriendRequestResponseDto;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendRequestMapper {

    @Mapping(source = "friendRequestEntity.status", target = "status")
    @Mapping(source = "friendRequestEntity.createdAt", target = "createDate")
    @Mapping(target = "fromUser", expression = "java(toFriendRequestUserDto(friendRequestEntity.getFromUser(), student))") // Aqui mapeando o fromUser
    FriendRequestResponseDto toFriendRequestResponseDto(FriendRequestEntity friendRequestEntity, StudentEntity student);

    default FriendRequestUserDto toFriendRequestUserDto(UserEntity user, StudentEntity student) {
        if (user == null) {
            return null;
        }
        return new FriendRequestUserDto(
                user.getPersonName(),
                user.getImageUrl(),
                student != null ? student.getCourseName() : null,
                student != null ? student.getUserPeriod() : null
        );
    }
}
