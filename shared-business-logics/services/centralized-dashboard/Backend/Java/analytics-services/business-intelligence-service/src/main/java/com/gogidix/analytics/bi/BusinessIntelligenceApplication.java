package com.gogidix.analytics.bi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Business Intelligence Service.
 * Provides BI reporting, dashboards, and analytics insights.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix")
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class BusinessIntelligenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessIntelligenceApplication.class, args);
    }
}
