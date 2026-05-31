package com.gogidix.customersupport.qualitymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Application class for Quality Management Service
 *
 * This service provides quality assurance functionality for customer support operations, including:
 * - QA review management and scoring
 * - Scorecard template management
 * - Calibration session management
 * - Agent quality tracking and reporting
 * - Quality metrics and analytics
 *
 * @port 8110
 * @author Gogidix
 * @version 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.customersupport.qualitymanagement")
@EnableMongoAuditing
@EnableAsync
public class QualityManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(QualityManagementServiceApplication.class);
        application.run(args);
    }
}
