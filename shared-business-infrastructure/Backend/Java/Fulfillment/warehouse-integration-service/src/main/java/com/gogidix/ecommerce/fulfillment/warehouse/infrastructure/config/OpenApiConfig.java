package com.gogidix.ecommerce.fulfillment.warehouse.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI warehouseServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Warehouse Service API")
                        .description("REST API for warehouse in the Gogidix E-commerce platform")
                        .version("1.0.0")
                        .contact(new Contact().name("Gogidix Development Team").email("dev@gogidix.com"))
                        .license(new License().name("Proprietary").url("https://gogidix.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8086").description("Development"),
                        new Server().url("https://api.gogidix.com").description("Production")))
                .addSecurityItem(new SecurityRequirement().addList("tenant-auth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("tenant-auth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-Tenant-ID")
                                        .description("Tenant ID for multi-tenancy support")));
    }
}