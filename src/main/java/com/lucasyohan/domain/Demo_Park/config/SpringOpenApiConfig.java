package com.lucasyohan.domain.Demo_Park.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringOpenApiConfig {

    @Bean
        public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("security", securityScheme()))
                .info(new Info()
                        .title("Demo Park API")
                        .version("1.0")
                        .license(new License()
                                .name("MIT License")
                                .url("https://apache.org/licenses/LICENSE-2.0.html"))
                        .contact(new Contact()
                                .name("Lucas Yohan")
                                .email("examplemail.com"))
                        .description("API para gerenciamento do estacionamento do Demo Park"));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .description("JWT Authentication")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Security");
    }

}
