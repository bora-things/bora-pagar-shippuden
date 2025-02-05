package com.borathings.borapagar.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    void testToDTO() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("ramon.jales.cr7@ufrn.edu.br")
                .name("Ramon Jales")
                .imageUrl("cr7.jpeg")
                .build();

        UserResponseDTO userDTO = userMapper.toUserResponseDTO(userEntity);

        assertEquals(userEntity.getId(), userDTO.id());
        assertEquals(userEntity.getEmail(), userDTO.email());
        assertEquals(userEntity.getName(), userDTO.name());
        assertEquals(userEntity.getImageUrl(), userDTO.imageUrl());
    }
}
