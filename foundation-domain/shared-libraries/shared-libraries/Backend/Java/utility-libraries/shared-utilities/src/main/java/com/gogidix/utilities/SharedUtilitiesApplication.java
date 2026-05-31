package com.gogidix.utilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

/**
 * Shared Utilities Application for the Gogidix Social E-commerce Ecosystem.
 * Provides utility functions, helpers, and common functionality.
 * 
 * Features:
 * - Date and time utilities with timezone support
 * - JSON serialization and deserialization utilities
 * - String manipulation and formatting utilities
 * - Collection processing and transformation utilities
 * - File handling and processing utilities
 * - Excel and CSV processing utilities
 * - HTTP and web utilities
 * - Validation and formatting utilities
 * - Math and calculation utilities
 * - Reflection and introspection utilities
 * 
 * NOTE: Eureka client auto-discovery is enabled via spring-cloud-starter-netflix-eureka-client
 * dependency. @EnableEurekaClient annotation is deprecated in Spring Cloud 2022.x+
 * 
 * @author Gogidix Development Team
 * @since 1.0.0
 * @version 1.0.0
 */
@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
public class SharedUtilitiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedUtilitiesApplication.class, args);
    }
}