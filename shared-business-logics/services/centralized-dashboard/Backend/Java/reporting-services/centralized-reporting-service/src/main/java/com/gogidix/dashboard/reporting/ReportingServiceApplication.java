package com.gogidix.dashboard.reporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main application class for Centralized Reporting Service.
 *
 * <p>This service implements centralized reporting and analytics
 * with multi-tenant SaaS architecture using hexagonal architecture.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>Report generation (PDF, Excel, CSV)</li>
 *   <li>Scheduled report execution</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Redis caching for report data</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableFeignClients
public class ReportingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportingServiceApplication.class, args);
    }
}
