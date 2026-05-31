package com.gogidix.sales.leadmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Lead Management Service Application
 * Main Spring Boot application class for the Lead Management microservice
 *
 * This service manages sales leads with the following features:
 * - Lead creation and capture from multiple sources (web, email, social, events)
 * - Lead scoring and qualification based on BANT methodology
 * - Lead assignment (round-robin, manual, territory-based)
 * - Lead stage management (new, contacted, qualified, converted, lost)
 * - Activity tracking and logging
 * - Duplicate lead detection
 * - Lead enrichment from external sources
 * - Multi-tenancy support
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.sales.leadmanagement")
@EnableMongoAuditing
@EnableKafka
@EnableAsync
public class LeadManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeadManagementServiceApplication.class, args);
    }
}
