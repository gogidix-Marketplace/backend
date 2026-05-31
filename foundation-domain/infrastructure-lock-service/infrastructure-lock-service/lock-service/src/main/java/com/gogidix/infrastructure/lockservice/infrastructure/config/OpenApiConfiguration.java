package com.gogidix.infrastructure.lockservice.infrastructure.config;

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

    @Value("${spring.application.name:Infrastructure Lock Service}")
    private String applicationName;

    @Value("${application.version:1.0.0}")
    private String applicationVersion;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title(applicationName)
                .description("Distributed locking service with Redis backend for multi-tenant resource management")
                .version(applicationVersion)
                .contact(new Contact()
                    .name("Gogidix")
                    .email("support@gogidix.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Development"),
                new Server().url("https://lock-service.gogidix.com").description("Production")
            ));
    }
}
