package com.gogidix.sales.dealmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Deal Management Service Application
 * Main Spring Boot application class for the Deal Management microservice
 *
 * This service manages sales deals with the following features:
 * - Deal creation and lifecycle management
 * - Pipeline/kanban view support
 * - Deal stage transitions with probability tracking
 * - Deal products and line items
 * - Competitor tracking and loss reasons
 * - Deal forecasting with weighted amounts
 * - Activity logging and timeline
 * - Deal collaboration and team selling
 * - Approval workflows for large deals
 * - Multi-tenancy support
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.sales.dealmanagement")
@EnableMongoAuditing
@EnableKafka
@EnableAsync
public class DealManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DealManagementServiceApplication.class, args);
    }
}
