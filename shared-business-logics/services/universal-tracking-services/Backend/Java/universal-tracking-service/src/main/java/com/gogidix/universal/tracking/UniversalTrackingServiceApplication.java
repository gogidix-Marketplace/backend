package com.gogidix.universal.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Universal Tracking Service.
 *
 * <p>This service provides universal event tracking and analytics capabilities
 * with multi-tenant SaaS architecture.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>Event tracking and session management</li>
 *   <li>Metrics collection and aggregation</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Redis caching for performance</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class UniversalTrackingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversalTrackingServiceApplication.class, args);
    }
}
