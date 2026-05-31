package com.gogidix.infrastructure.database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for Infrastructure Database Service.
 *
 * <p>Database infrastructure services providing:</p>
 * <ul>
 *   <li>Database connection pooling management</li>
 *   <li>Database migration management (Flyway/Liquibase)</li>
 *   <li>Multi-tenant database routing</li>
 *   <li>Database backup/restore coordination</li>
 *   <li>Query performance monitoring and metrics</li>
 *   <li>Database health checks and diagnostics</li>
 *   <li>Distributed transaction coordination</li>
 * </ul>
 *
 * <p>Architecture:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD principles</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>CQRS pattern for command/query separation</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Redis caching for fast metadata access</li>
 *   <li>MongoDB for metadata storage</li>
 *   <li>Support for PostgreSQL and other databases</li>
 * </ul>
 *
 * @author Gogidix Platform Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
public class InfrastructureDatabaseServiceApplication {

    private static final String PROFILE_DEFAULT = "spring.profiles.active";

    /**
     * Main entry point for the Infrastructure Database Service.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(InfrastructureDatabaseServiceApplication.class);
        app.setAdditionalProfiles(getDefaultProfiles().split(","));
        app.run(args);
    }

    /**
     * Gets the default Spring profiles to activate.
     * Falls back to 'dev' if no profile is specified.
     *
     * @return Comma-separated profile names
     */
    private static String getDefaultProfiles() {
        String profiles = System.getProperty(PROFILE_DEFAULT, System.getenv(PROFILE_DEFAULT));
        return (profiles != null && !profiles.isEmpty()) ? profiles : "dev";
    }
}
