package com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

/**
 * Web configuration for Global Business Dashboard Service.
 * Configures CORS, date formatting, and other web-related settings.
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] ALLOWED_ORIGINS = {
        "http://localhost:3000",
        "http://localhost:3001",
        "http://localhost:4200",
        "http://localhost:8080",
        "https://*.gogidix.com",
        "https://gogidix.com"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Configuring CORS mappings for allowed origins: {}", String.join(", ", ALLOWED_ORIGINS));

        registry.addMapping("/api/**")
            .allowedOrigins(ALLOWED_ORIGINS)
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Location", "ETag", "X-Total-Count", "X-Page-Count")
            .maxAge(3600)
            .allowCredentials(true);

        registry.addMapping("/actuator/**")
            .allowedOrigins("http://localhost:3000", "http://localhost:4200")
            .allowedMethods("GET")
            .allowedHeaders("*");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ISO_LOCAL_DATE);
        registrar.setDateTimeFormatter(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        registrar.registerFormatters(registry);
        log.info("Registered date/time formatters");
    }
}
