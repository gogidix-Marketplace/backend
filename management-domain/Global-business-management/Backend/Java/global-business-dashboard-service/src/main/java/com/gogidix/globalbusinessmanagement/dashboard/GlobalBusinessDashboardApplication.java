package com.gogidix.globalbusinessmanagement.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main application class for Global Business Dashboard Service.
 * This service provides comprehensive dashboard functionality for global business management.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.globalbusinessmanagement")
@EnableMongoAuditing
@EnableCaching
public class GlobalBusinessDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalBusinessDashboardApplication.class, args);
    }
}
