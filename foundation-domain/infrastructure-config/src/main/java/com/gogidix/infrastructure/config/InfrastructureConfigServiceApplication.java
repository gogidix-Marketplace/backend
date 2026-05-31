package com.gogidix.infrastructure.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Infrastructure Config Service.
 *
 * <p>Centralized configuration management service providing:</p>
 * <ul>
 *   <li>Configuration properties management with versioning</li>
 *   <li>Environment-specific configs (dev, staging, prod)</li>
 *   <li>Feature flags/toggles with dynamic updates</li>
 *   <li>Secret management integration with encryption</li>
 *   <li>Configuration validation and audit trail</li>
 *   <li>Dynamic config updates with cache invalidation</li>
 * </ul>
 *
 * <p>Architecture:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD principles</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>CQRS pattern for command/query separation</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Redis caching for fast config access</li>
 *   <li>MongoDB for persistent storage</li>
 * </ul>
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
public class InfrastructureConfigServiceApplication {

    private static final String PROFILE_DEFAULT = "spring.profiles.active";

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(InfrastructureConfigServiceApplication.class);
        app.setAdditionalProfiles(getDefaultProfiles().split(","));
        app.run(args);
    }

    private static String getDefaultProfiles() {
        String profiles = System.getProperty(PROFILE_DEFAULT, System.getenv(PROFILE_DEFAULT));
        return (profiles != null && !profiles.isEmpty()) ? profiles : "dev";
    }
}
