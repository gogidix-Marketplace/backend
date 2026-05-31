package com.gogidix.dashboard.aggregation.infrastructure.config;

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
 * OpenAPI configuration for data aggregation service.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8905}")
    private int serverPort;

    @Bean
    public OpenAPI dataAggregationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Data Aggregation Service API")
                        .description("Cross-service data aggregation with hexagonal architecture")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gogidix Platform Team")
                                .email("platform@gogidix.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server"),
                        new Server()
                                .url("https://api.gogidix.com")
                                .description("Production Server")));
    }
}
