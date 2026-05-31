package com.gogidix.finance.compliance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Compliance Service Application
 * Multi-tenant SaaS hexagonal architecture for Finance Department
 * Handles compliance rules, checks, and reporting
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class ComplianceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComplianceApplication.class, args);
    }
}
