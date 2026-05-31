package com.gogidix.dashboard.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main application class for Dashboard Core Service.
 *
 * <p>This service implements core dashboard logic and KPI management
 * with multi-tenant SaaS architecture using hexagonal architecture.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>CQRS pattern for command/query separation</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Redis caching for performance</li>
 *   <li>KPI management and analytics</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableFeignClients
public class DashboardCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboardCoreApplication.class, args);
    }
}
