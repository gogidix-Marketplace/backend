package com.gogidix.customersupport.ticketmanagement.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ticketManagementOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8103");
        server.setDescription("Development Server");

        Contact contact = new Contact();
        contact.setEmail("support@gogidix.com");
        contact.setName("Gogidix Support Team");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Ticket Management Service API")
                .version("1.0.0")
                .description("RESTful APIs for managing support tickets with assignment workflows, status tracking, and escalation")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
