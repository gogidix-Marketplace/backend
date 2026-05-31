package com.gogidix.ecommerce.pushnotification.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI PushNotificationOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("PushNotification Service API")
                .version("1.0.0")
                .description("REST API for PushNotification Service")
                .contact(new Contact()
                    .name("Gogidix")
                    .email("dev@gogidix.com")));
    }
}