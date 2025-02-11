package com.borathings.borapagar.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.borathings.borapagar.user.dto.UserDTO;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.security.oauth2.core.user.OAuth2User;

class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToDTO() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("ramon.jales.cr7@ufrn.edu.br")
                .personName("Ramon Jales")
                .imageUrl("cr7.jpeg")
                .build();

        UserResponseDTO userDTO = userMapper.toUserResponseDTO(userEntity);

        assertEquals(userEntity.getId(), userDTO.id());
        assertEquals(userEntity.getEmail(), userDTO.email());
//        assertEquals(userEntity.getPersonName(), userDTO.name());
        assertEquals(userEntity.getImageUrl(), userDTO.imageUrl());
    }

    @Test
    void testToUserResponseDTO() {
        UserEntity user = UserEntity.builder()
                .userId(123)
                .email("user@example.com")
                .personName("User")
                .login("user123")
                .institutionalId(123456789L)
                .cpf("12345678901")
                .imageUrl("http://example.com/image.jpg")
                .build();

        UserResponseDTO responseDTO = userMapper.toUserResponseDTO(user);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.email()).isEqualTo(user.getEmail());
//        assertThat(responseDTO.name()).isEqualTo(user.getPersonName());
    }

    @Test
    void testToDto() {
        UserEntity user = UserEntity.builder()
                .userId(123)
                .email("user@example.com")
                .personName("User")
                .login("user123")
                .institutionalId(123456789L)
                .cpf("12345678901")
                .imageUrl("http://example.com/image.jpg")
                .build();

        UserDTO dto = userMapper.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.email()).isEqualTo(user.getEmail());
        assertThat(dto.personName()).isEqualTo(user.getPersonName());
    }

    @Test
    void testToEntity() {
        UserDTO dto = new UserDTO(
                "user123",
                "user@example.com",
                "User",
                123,
                111L,
                "123456789",
                "http://example.com/image.jpg",
                false);
        UserEntity entity = userMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getEmail()).isEqualTo(dto.email());
        assertThat(entity.getPersonName()).isEqualTo(dto.personName());
    }

    @Test
    void testFromSigaaUser() {
        OAuth2User mockUser = Mockito.mock(OAuth2User.class);
        Mockito.when(mockUser.getName()).thenReturn("user123");
        Mockito.when(mockUser.getAttribute("email")).thenReturn("user@example.com");
        Mockito.when(mockUser.getAttribute("nome-pessoa")).thenReturn("User");
        Mockito.when(mockUser.getAttribute("id-usuario")).thenReturn(123);
        Mockito.when(mockUser.getAttribute("id-institucional")).thenReturn(123456789L);
        Mockito.when(mockUser.getAttribute("cpf-cnpj")).thenReturn("12345678901");
        Mockito.when(mockUser.getAttribute("url-foto")).thenReturn("http://example.com/image.jpg");

        UserDTO dto = UserDTO.fromSigaaUser(mockUser);

        assertThat(dto).isNotNull();
        assertThat(dto.email()).isEqualTo("user@example.com");
//        assertThat(dto.personName()).isEqualTo("User");
        assertThat(dto.deleted()).isFalse();
    }
}
