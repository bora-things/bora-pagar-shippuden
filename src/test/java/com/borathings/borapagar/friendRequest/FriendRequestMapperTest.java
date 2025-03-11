package com.borathings.borapagar.friendRequest;

import static org.junit.jupiter.api.Assertions.*;

import com.borathings.borapagar.friendRequest.dto.FriendRequestUserDto;
import com.borathings.borapagar.friendRequest.dto.response.FriendRequestResponseDto;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class FriendRequestMapperTest {

    private FriendRequestMapper friendRequestMapper;

    @BeforeEach
    void setUp() {
        friendRequestMapper = Mappers.getMapper(FriendRequestMapper.class);
    }

    @Test
    void testToFriendRequestResponseDto() {
        FriendRequestEntity friendRequestEntity = new FriendRequestEntity();
        UserEntity user = new UserEntity();
        user.setPersonName("John Doe");
        user.setImageUrl("https://example.com/image.jpg");
        friendRequestEntity.setFromUser(user);
        friendRequestEntity.setStatus(FriendRequestStatus.PENDING);

        StudentEntity student = new StudentEntity();
        student.setCourseName("Computer Science");
        FriendRequestResponseDto responseDto =
                friendRequestMapper.toFriendRequestResponseDto(friendRequestEntity, student);

        assertNotNull(responseDto);
        assertEquals(FriendRequestStatus.PENDING, responseDto.getStatus());
        assertEquals("John Doe", responseDto.getFromUser().personName());
        assertEquals("https://example.com/image.jpg", responseDto.getFromUser().imageUrl());
        assertEquals("Computer Science", responseDto.getFromUser().courseName());
        assertEquals(0, responseDto.getFromUser().period());
    }

    @Test
    void testToFriendRequestUserDto_WithNullStudent() {
        UserEntity user = new UserEntity();
        user.setPersonName("Jane Doe");
        user.setImageUrl("https://example.com/jane.jpg");

        FriendRequestUserDto userDto = friendRequestMapper.toFriendRequestUserDto(user, null);

        assertNotNull(userDto);
        assertEquals("Jane Doe", userDto.personName());
        assertEquals("https://example.com/jane.jpg", userDto.imageUrl());
        assertNull(userDto.courseName());
        assertNull(userDto.period());
    }
}
