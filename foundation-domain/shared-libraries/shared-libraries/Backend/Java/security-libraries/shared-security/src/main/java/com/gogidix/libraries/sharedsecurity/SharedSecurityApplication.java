package com.gogidix.libraries.sharedsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Shared Security Library Service for the Gogidix Social E-commerce Ecosystem.
 * 
 * HEXAGONAL ARCHITECTURE:
 * ├── Domain Layer (com.gogidix.libraries.sharedsecurity.domain)
 * │   ├── model/ - Security entities and aggregates
 * │   ├── service/ - Domain security services
 * │   ├── valueobject/ - Security value objects
 * │   └── enums/ - Security-related enumerations
 * ├── Application Layer (com.gogidix.libraries.sharedsecurity.application)
 * │   ├── service/ - Application security services
 * │   ├── dto/ - Data Transfer Objects
 * │   └── port/ - Input/Output ports
 * └── Infrastructure Layer (com.gogidix.libraries.sharedsecurity.infrastructure)
 *     ├── config/ - Configuration classes
 *     ├── security/ - Security configurations
 *     └── adapter/ - External adapters
 * 
 * SECURITY FEATURES:
 * - JWT token generation, validation, and refresh
 * - Spring Security configuration with OAuth2
 * - Authentication filters and security entry points
 * - User principal and role management
 * - Session management with Redis
 * - Security event logging and monitoring
 * - Rate limiting and brute force protection
 * - CORS and CSRF protection
 * - API security with method-level authorization
 * 
 * PORT: 8302 (Shared Security Library Service)
 * DISCOVERY: shared-security-service
 * 
 * @author Gogidix Development Team
 * @since 1.0.0
 * @version 1.0.0
 */
@SpringBootApplication(
    exclude = {
        FlywayAutoConfiguration.class,
        DataSourceAutoConfiguration.class
    }
)
@EnableDiscoveryClient
@EnableWebSecurity
@EnableCaching
@ComponentScan(basePackages = {
    "com.gogidix.libraries.sharedsecurity",
    "com.gogidix.ecosystem.shared.security",
    "com.gogidix.shared.security"
})
public class SharedSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedSecurityApplication.class, args);
    }
}