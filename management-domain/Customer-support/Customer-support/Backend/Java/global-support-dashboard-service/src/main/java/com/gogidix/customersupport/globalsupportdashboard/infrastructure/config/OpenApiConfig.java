package com.gogidix.customersupport.globalsupportdashboard.infrastructure.config;

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
 * OpenAPI/Swagger Configuration
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8100}")
    private int serverPort;

    @Bean
    public OpenAPI globalSupportOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Global Support Dashboard Service API")
                        .description("REST API for Global Support Dashboard - Provides global-level analytics and metrics for customer support operations")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gogidix Support")
                                .email("support@gogidix.com")
                                .url("https://gogidix.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.gogidix.com")
                                .description("Production Server")));
    }
}
