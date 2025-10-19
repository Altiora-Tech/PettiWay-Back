package com.altioratech.pettiway.user.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI pettiWayOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("PettiWay API")
                        .description("Documentación interactiva de la API PettiWay - Usuarios, autenticación y más.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo PettiWay")
                                .email("contact@pettiway.com")
                                .url("https://pettiway.com")));
    }
}