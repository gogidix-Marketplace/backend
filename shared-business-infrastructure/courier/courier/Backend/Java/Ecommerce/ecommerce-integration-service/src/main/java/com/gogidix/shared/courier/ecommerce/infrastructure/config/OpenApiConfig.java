package com.gogidix.shared.courier.ecommerce.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecommerceIntegrationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Courier Integration API")
                        .description("Zone-based courier assignment and tracking for e-commerce delivery")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gogidix Engineering")
                                .email("engineering@gogidix.com")));
    }
}
