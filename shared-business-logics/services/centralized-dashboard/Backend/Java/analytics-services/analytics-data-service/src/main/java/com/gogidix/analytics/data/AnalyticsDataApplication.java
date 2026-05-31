package com.gogidix.analytics.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Analytics Data Service.
 * Provides data API for analytics queries and exports.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix")
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class AnalyticsDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsDataApplication.class, args);
    }
}
