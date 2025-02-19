package com.borathings.borapagar.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Bora Pagar")
                        .description("Ferramenta de auxílio ao perído de matrícula")
                        .contact(new Contact().email("boraPagar@gmail.com"))
                        .license(new License().name("License"))
                        .version("v2"));
    }
}
