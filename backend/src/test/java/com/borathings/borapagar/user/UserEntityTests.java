package com.borathings.borapagar.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserEntityTest {

    private UserEntity user1;
    private UserEntity user2;
    private UserEntity user3;

    @BeforeEach
    void setUp() {
        user1 = UserEntity.builder()
                .userId(123)
                .email("user1@example.com")
                .personName("User One")
                .login("user1")
                .institutionalId(123456789L)
                .cpf("12345678901")
                .imageUrl("http://example.com/image1.jpg")
                .build();

        user2 = UserEntity.builder()
                .userId(123) // Same ID as user1
                .email("user2@example.com")
                .personName("User Two")
                .login("user2")
                .institutionalId(987654321L)
                .cpf("10987654321")
                .imageUrl("http://example.com/image2.jpg")
                .build();

        user3 = UserEntity.builder()
                .userId(456) // Different ID
                .email("user3@example.com")
                .personName("User Three")
                .login("user3")
                .institutionalId(192837465L)
                .cpf("19283746509")
                .imageUrl("http://example.com/image3.jpg")
                .build();
    }

    @Test
    void testEquals_withSameIdUsuario_shouldBeEqual() {
        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void testEquals_withDifferentIdUsuario_shouldNotBeEqual() {
        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    void testHashCode_withSameIdUsuario_shouldBeEqual() {
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    void testHashCode_withDifferentIdUsuario_shouldNotBeEqual() {
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }
}
