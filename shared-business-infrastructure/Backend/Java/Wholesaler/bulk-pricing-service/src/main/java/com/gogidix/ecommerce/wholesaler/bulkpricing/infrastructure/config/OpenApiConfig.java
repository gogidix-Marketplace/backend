package com.gogidix.ecommerce.wholesaler.bulkpricing.infrastructure.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI bulkPricingServiceOpenAPI() {
        return new OpenAPI().info(new Info().title("Bulk Pricing Service API").description("REST API for bulk pricing in the Gogidix E-commerce platform").version("1.0.0"));
    }
}
