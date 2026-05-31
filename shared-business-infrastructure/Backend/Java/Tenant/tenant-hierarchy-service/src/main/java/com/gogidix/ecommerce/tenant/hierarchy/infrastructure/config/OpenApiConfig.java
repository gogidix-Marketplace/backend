package com.gogidix.ecommerce.tenant.hierarchy.infrastructure.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI tenantHierarchyServiceOpenAPI() {
        return new OpenAPI().info(new Info().title("Tenant Hierarchy Service API").description("REST API for tenant hierarchy in the Gogidix E-commerce platform").version("1.0.0"));
    }
}
