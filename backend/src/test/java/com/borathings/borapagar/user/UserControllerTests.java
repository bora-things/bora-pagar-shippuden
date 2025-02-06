package com.borathings.borapagar.user;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserMapper userMapper() {
            return new UserMapperImpl();
        }
    }

    private UserEntity user;

    @BeforeEach
    public void setUp() {

        user = UserEntity.builder()
                .id(1L)
                .name("Isaac")
                .email("isaac.lourenco.704@ufrn.edu.br")
                .imageUrl("https://lindao.com")
                .build();
    }

    @Test
    public void shouldGetAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/api/users/me").with(oidcLogin())).andExpect(status().isOk());
    }
}
