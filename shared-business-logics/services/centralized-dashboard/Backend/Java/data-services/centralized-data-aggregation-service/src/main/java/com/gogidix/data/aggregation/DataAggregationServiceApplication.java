package com.gogidix.data.aggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main application class for Centralized Data Aggregation Service.
 *
 * <p>This service implements cross-service data aggregation
 * with multi-tenant SaaS architecture using hexagonal architecture.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>Feign clients for external service calls</li>
 *   <li>Redis caching for aggregated data</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Scheduled batch aggregation jobs</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableFeignClients
public class DataAggregationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAggregationServiceApplication.class, args);
    }
}
