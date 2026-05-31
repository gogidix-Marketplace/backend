package com.gogidix.ecommerce.tracking.realtime.infrastructure.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI realtimeTrackingServiceOpenAPI() {
        return new OpenAPI().info(new Info().title("Realtime Tracking Service API").description("REST API for realtime tracking in the Gogidix E-commerce platform").version("1.0.0"));
    }
}
