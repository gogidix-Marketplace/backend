package com.gogidix.digitalmarketing.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Digital Marketing Analytics Service Application
 *
 * <p>This service tracks marketing metrics, ROI, attribution, and provides analytics dashboards.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Marketing metrics tracking (impressions, clicks, conversions)</li>
 *   <li>Campaign performance analytics</li>
 *   <li>Channel performance comparison</li>
 *   <li>Attribution tracking across touchpoints</li>
 *   <li>ROI calculation and reporting</li>
 *   <li>Dashboard metrics aggregation</li>
 *   <li>Tenant-isolated analytics</li>
 * </ul>
 *
 * @author Gogidix
 * @version 1.0.0
 */
@SpringBootApplication(
    scanBasePackages = {
        "com.gogidix.digitalmarketing.analytics",
        "com.gogidix.digitalmarketing.shared"
    }
)
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.gogidix.digitalmarketing.analytics")
@EnableCaching
@EnableAsync
public class AnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsServiceApplication.class, args);
    }
}
