package com.gogidix.courier.routingservice.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for routing service.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS mappings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("X-Correlation-Id", "X-Tenant-Id", "X-User-Id")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
