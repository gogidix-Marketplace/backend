package com.gogidix.finance.budgettracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Budget Tracking Service Application
 *
 * Main Spring Boot application for the Budget Tracking Service.
 * Implements hexagonal architecture for multi-tenant budget tracking,
 * variance analysis, and threshold monitoring.
 *
 * @package com.gogidix.finance.budgettracking
 */
@SpringBootApplication
@EnableKafka
@EnableMongoAuditing
public class BudgetTrackingApplication {

    private static final String APPLICATION_NAME = "Budget Tracking Service";

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BudgetTrackingApplication.class);
        app.setAdditionalProfiles("default");
        app.run(args);

        System.out.println("=================================================");
        System.out.println("  " + APPLICATION_NAME + " Started Successfully");
        System.out.println("=================================================");
    }
}
