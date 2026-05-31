package com.gogidix.dashboard.aggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Data Aggregation Service.
 *
 * <p>This service implements cross-service data aggregation
 * with multi-tenant SaaS architecture using hexagonal architecture.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>Cross-domain data aggregation</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Redis caching for performance</li>
 *   <li>Feign clients for service integration</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableFeignClients
public class DataAggregationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAggregationApplication.class, args);
    }
}
