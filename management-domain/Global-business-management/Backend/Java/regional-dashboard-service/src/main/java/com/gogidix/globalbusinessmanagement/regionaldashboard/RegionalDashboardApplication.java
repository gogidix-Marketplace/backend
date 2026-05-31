package com.gogidix.globalbusinessmanagement.regionaldashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main application class for Regional Dashboard Service.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.globalbusinessmanagement")
@EnableMongoAuditing
@EnableCaching
public class RegionalDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegionalDashboardApplication.class, args);
    }
}
