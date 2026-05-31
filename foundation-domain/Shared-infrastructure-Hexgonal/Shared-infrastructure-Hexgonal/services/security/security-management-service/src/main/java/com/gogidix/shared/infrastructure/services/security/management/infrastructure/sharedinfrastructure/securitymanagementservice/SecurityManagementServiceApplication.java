package com.gogidix.shared.infrastructure.services.security.management.sharedinfrastructure.securitymanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Security Management Service
 * Provides comprehensive security management, policy controls, and threat detection
 * Multi-tenant MongoDB architecture
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
@EnableMongoRepositories(basePackages = "com.gogidix.infrastructure")
@EnableMongoAuditing
public class SecurityManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityManagementServiceApplication.class, args);
    }
}
