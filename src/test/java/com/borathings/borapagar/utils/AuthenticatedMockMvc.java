package com.borathings.borapagar.utils;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@TestConfiguration
public class AuthenticatedMockMvc {
    @Bean
    public MockMvc buildMockMvc(WebApplicationContext wac) {
        return MockMvcBuilders.webAppContextSetup(wac)
                .defaultRequest(get("/").with(oidcLogin()))
                .build();
    }
}
