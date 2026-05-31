package com.gogidix.ecommerce.wholesaler.contract.infrastructure.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI contractManagementServiceOpenAPI() {
        return new OpenAPI().info(new Info().title("Contract Management Service API").description("REST API for contract management in the Gogidix E-commerce platform").version("1.0.0"));
    }
}
