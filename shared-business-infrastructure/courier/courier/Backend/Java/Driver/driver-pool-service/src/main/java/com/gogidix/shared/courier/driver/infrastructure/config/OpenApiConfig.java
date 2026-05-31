package com.gogidix.shared.courier.driver.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI configuration for Driver Pool Service
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "tenant-header";

    @Bean
    public OpenAPI driverPoolServiceOpenAPI() {
        final String securitySchemeName = "tenant-header";

        return new OpenAPI()
                .info(new Info()
                        .title("Driver Pool Service API")
                        .description("Multi-tenant driver pool management service with real-time geospatial tracking capabilities. " +
                                "This service provides endpoints for managing driver profiles, locations, availability status, " +
                                "and finding nearby drivers using MongoDB geospatial indexing.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gogidix Platform Team")
                                .email("platform@gogidix.com")
                                .url("https://gogidix.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://gogidix.com/license")))
                .servers(List.of(
                        new Server().url("http://localhost:8084").description("Local Development"),
                        new Server().url("https://api.gogidix.com/driver-pool").description("Production")
                ))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-Tenant-ID")
                                        .description("Tenant identifier header - required for all requests")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

    @Bean
    public GroupedOpenApi driverApi() {
        return GroupedOpenApi.builder()
                .group("driver-pool")
                .pathsToMatch("/api/v1/drivers/**")
                .build();
    }

    @Bean
    public GroupedOpenApi healthApi() {
        return GroupedOpenApi.builder()
                .group("health")
                .pathsToMatch("/health/**")
                .build();
    }
}
