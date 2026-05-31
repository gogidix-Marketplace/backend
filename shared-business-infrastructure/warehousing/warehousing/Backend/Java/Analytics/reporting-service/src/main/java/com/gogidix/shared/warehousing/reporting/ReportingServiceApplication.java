package com.gogidix.shared.warehousing.reporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Reporting Service Application
 *
 * Multi-tenant MongoDB service for warehouse reporting and analytics
 */
@SpringBootApplication
@EnableMongoAuditing
public class ReportingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportingServiceApplication.class, args);
    }
}
