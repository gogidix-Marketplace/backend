package com.gogidix.sales.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * CRM Service Application
 * Multi-tenant SaaS hexagonal architecture for Sales Department
 *
 * This service manages:
 * - Customer lifecycle (lead, prospect, customer, churned)
 * - Contact management with relationships
 * - Customer interactions tracking (calls, emails, meetings)
 * - Customer segmentation
 * - Account hierarchy support
 * - Multi-tenancy support
 *
 * @author Gogidix
 * @version 1.0.0
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class CrmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmServiceApplication.class, args);
    }
}
