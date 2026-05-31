package com.gogidix.shared.utilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Shared Utilities Application for the Gogidix Social E-commerce Ecosystem.
 * 
 * This service provides a comprehensive set of utility functions following
 * Hexagonal Architecture principles with clear separation of concerns.
 * 
 * Key Features:
 * - Date and time utilities with timezone support
 * - JSON serialization and deserialization utilities
 * - String manipulation and formatting utilities  
 * - Collection processing and transformation utilities
 * - File handling (Excel, CSV, text) utilities
 * - HTTP and web utilities
 * - Validation and formatting utilities
 * - Mathematical and statistical calculations
 * - Cryptographic utilities (hashing, encoding)
 * - Reflection and introspection utilities
 * 
 * Architecture:
 * - Domain Layer: Core business logic and domain models
 * - Application Layer: Use cases and application services
 * - Infrastructure Layer: External adapters and implementations
 * - API Layer: REST endpoints and DTOs
 * 
 * @author Gogidix Development Team
 * @version 1.0.0
 * @since 2025-01-01
 */
@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
@EnableCaching
@EnableAsync
@EnableJpaRepositories
@EntityScan
public class SharedUtilitiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedUtilitiesApplication.class, args);
    }
}