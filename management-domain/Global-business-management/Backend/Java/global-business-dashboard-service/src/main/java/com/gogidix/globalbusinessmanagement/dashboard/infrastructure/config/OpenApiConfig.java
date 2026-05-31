package com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI configuration for Global Business Dashboard Service.
 * Configures Swagger/OpenAPI documentation.
 */
@Slf4j
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8200}")
    private int serverPort;

    @Value("${spring.application.name:global-business-dashboard-service}")
    private String applicationName;

    @Value("${application.version:1.0.0}")
    private String applicationVersion;

    @Bean
    public OpenAPI customOpenAPI() {
        log.info("Configuring OpenAPI for application: {}", applicationName);

        return new OpenAPI()
            .components(new Components()
                .addSchemas("ErrorResponse", createErrorResponseSchema()))
            .info(new Info()
                .title("Global Business Dashboard Service API")
                .description("REST API for Global Business Dashboard Management. Provides endpoints for " +
                    "managing global business metrics, regional summaries, country metrics, and KPI boards.")
                .version(applicationVersion)
                .contact(new Contact()
                    .name("Gogidix Development Team")
                    .email("dev@gogidix.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Local Development Server"),
                new Server()
                    .url("https://api.gogidix.com")
                    .description("Production Server")
            ));
    }

    private Schema<?> createErrorResponseSchema() {
        return new Schema<>()
            .type("object")
            .addProperty("timestamp", new Schema<>().type("string").format("date-time")
                .description("The timestamp when the error occurred"))
            .addProperty("status", new Schema<>().type("integer").description("HTTP status code"))
            .addProperty("error", new Schema<>().type("string").description("Error type"))
            .addProperty("message", new Schema<>().type("string").description("Error message"))
            .addProperty("path", new Schema<>().type("string").description("Request path"))
            .addProperty("details", new Schema<>().type("string").description("Detailed error information"));
    }
}
