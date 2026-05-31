package com.gogidix.foundation.devtools.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration.
 */
@Configuration
public class OpenApiConfiguration {

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    public OpenAPI devToolsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Infrastructure DevTools API")
                        .description("Development tools and utilities for the Gogidix platform including API testing, database queries, logging, deployment, and documentation generation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gogidix Platform Team")
                                .email("platform@gogidix.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort + "/api/devtools")
                                .description("Local development server"),
                        new Server()
                                .url("https://devtools.gogidix.com")
                                .description("Production server")));
    }
}
