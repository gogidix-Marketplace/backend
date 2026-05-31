package com.gogidix.ecommerce.paymentmethod.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI PaymentMethodOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("PaymentMethod Service API")
                .version("1.0.0")
                .description("REST API for PaymentMethod Service")
                .contact(new Contact()
                    .name("Gogidix")
                    .email("dev@gogidix.com")));
    }
}