package com.gogidix.analytics.metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Metrics Aggregation Service.
 * Aggregates platform metrics across all services in the ecosystem.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix")
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class MetricsAggregationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetricsAggregationApplication.class, args);
    }
}
