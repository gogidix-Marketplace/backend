package com.gogidix.courier.etaservice.infrastructure.config;

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
 * OpenAPI configuration for ETA service documentation.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8108}")
    private int serverPort;

    @Value("${spring.application.name:eta-service}")
    private String appName;

    @Bean
    public OpenAPI etaServiceOpenAPI() {
        String serverUrl = "http://localhost:" + serverPort + "/api/v1";

        return new OpenAPI()
                .info(new Info()
                        .title("ETA Service API")
                        .description("Estimated Time of Arrival Service for GOGIDIX Courier Services. " +
                                "Provides traffic-aware ETA calculations with real-time updates.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("GOGIDIX Development Team")
                                .email("dev@gogidix.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://www.gogidix.com")))
                .servers(List.of(
                        new Server().url(serverUrl).description("Local Development Server"),
                        new Server().url("https://api.gogidix.com/eta").description("Production Server")
                ));
    }
}
