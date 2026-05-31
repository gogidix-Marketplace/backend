package com.gogidix.monitoring.performance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main application class for Centralized Performance Monitoring Service.
 *
 * <p>This service implements centralized performance monitoring and metrics collection
 * with multi-tenant SaaS architecture using hexagonal architecture.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>Time-series metrics storage with MongoDB</li>
 *   <li>Redis caching for real-time metrics</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Performance alerting and anomaly detection</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableFeignClients
public class PerformanceMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformanceMonitoringApplication.class, args);
    }
}
