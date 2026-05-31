package com.gogidix.digitalmarketing.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Budget Management Service Application
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.digitalmarketing.analytics",
    "com.gogidix.digitalmarketing.shared"
})
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
public class BudgetManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetManagementServiceApplication.class, args);
    }
}
