package com.gogidix.shared.warehousing.fulfillment.picking.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI Configuration for Picking Service
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI pickingServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Picking Service API")
                        .description("Multi-tenant Warehouse Picking Order Management API for Gogidix Ecosystem")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gogidix Development Team")
                                .email("dev@gogidix.com")
                                .url("https://gogidix.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("https://api.gogidix.com").description("Production Server")
                ));
    }

    @Bean
    public GroupedOpenApi pickingApi() {
        return GroupedOpenApi.builder()
                .group("picking")
                .pathsToMatch("/api/v1/fulfillment/picking/**")
                .build();
    }

    @Bean
    public GroupedOpenApi healthApi() {
        return GroupedOpenApi.builder()
                .group("health")
                .pathsToMatch("/health/**", "/actuator/**")
                .build();
    }
}
