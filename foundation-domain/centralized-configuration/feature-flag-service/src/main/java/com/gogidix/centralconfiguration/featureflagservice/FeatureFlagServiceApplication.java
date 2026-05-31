package com.gogidix.centralconfiguration.featureflagservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Feature Flag Service.
 *
 * <p>Feature toggle management service for dynamic feature control
 * with multi-tenant SaaS architecture.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>CQRS pattern for command/query separation</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Redis caching for fast feature flag evaluation</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class FeatureFlagServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeatureFlagServiceApplication.class, args);
    }
}
