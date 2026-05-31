package com.gogidix.sales.onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Customer Onboarding Service Application
 *
 * Hexagonal Architecture SaaS multi-tenant service for customer onboarding workflow management
 * in the Sales Department.
 *
 * Architecture Layers:
 * - Domain: Entities, Events, Ports (in/out), Value Objects, Repository Interfaces
 * - Application: Command/Query Services, DTOs, Mappers
 * - Infrastructure: MongoDB Repositories, Kafka Event Publishing, Email Service, Configuration
 * - Interface: REST Controllers, Exception Handling
 *
 * Features:
 * - Customer onboarding workflow management
 * - Onboarding step tracking with dependencies
 * - Onboarding templates for different customer types
 * - Document checklist management
 * - Onboarding progress tracking
 * - Automated task assignments
 * - Welcome email sequences
 * - Multi-tenancy with RequestContextHolder
 * - Domain events (OnboardingStartedEvent, OnboardingCompletedEvent, OnboardingStepCompletedEvent)
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
@EnableAsync
@EnableScheduling
public class CustomerOnboardingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerOnboardingServiceApplication.class, args);
    }
}
