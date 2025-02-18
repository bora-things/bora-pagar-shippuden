package com.borathings.borapagar.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PatcherTests {
    @Autowired
    Patcher<UserEntity> patcher;

    @Autowired
    @Qualifier("userMapperImpl")
    UserMapper mapper;

    @Test
    public void testPatcher() {
        UserEntity target = UserEntity.builder()
                .id(1L)
                .email("teste@gmail.com")
                .personName("Ian Gabriel")
                .imageUrl("teste.com")
                .build();

        UserEntity source = UserEntity.builder()
                .email("editado@gmail.com")
                .imageUrl("editado.com")
                .build();
        assertDoesNotThrow(() -> patcher.patch(target, source));
        assertEquals(target.getEmail(), source.getEmail());
        assertEquals(target.getImageUrl(), source.getImageUrl());
    }
}
