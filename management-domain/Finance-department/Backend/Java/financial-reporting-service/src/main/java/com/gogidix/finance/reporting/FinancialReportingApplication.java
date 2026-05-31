package com.gogidix.finance.reporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Financial Reporting Service Application
 * Multi-tenant SaaS hexagonal architecture for Finance Department
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableAsync
@EnableScheduling
public class FinancialReportingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialReportingApplication.class, args);
    }
}
