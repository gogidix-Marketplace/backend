package com.gogidix.courier.assignmentservice.infrastructure.config;

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
 * OpenAPI configuration for assignment service.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8102}")
    private int serverPort;

    @Bean
    public OpenAPI assignmentServiceOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:" + serverPort + "/api/v1");
        server.setDescription("Development Server");

        Contact contact = new Contact();
        contact.setName("GOGIDIX Courier Services");
        contact.setEmail("support@gogidix.com");

        License license = new License()
                .name("Proprietary")
                .url("https://gogidix.com/terms");

        Info info = new Info()
                .title("Assignment Service API")
                .version("1.0.0")
                .description("Driver Assignment Optimization Service for GOGIDIX Courier Services. " +
                        "Provides intelligent driver-to-dispatch assignment with real-time optimization.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
