package com.gogidix.customersupport.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Application class for Feedback Service
 *
 * This service provides feedback collection and analysis functionality including:
 * - CSAT (Customer Satisfaction) surveys
 * - NPS (Net Promoter Score) collection
 * - Customer feedback aggregation and reporting
 * - Survey template management
 * - Feedback analytics and trends
 *
 * @port 8111
 * @author Gogidix
 * @version 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.customersupport.feedback")
@EnableMongoAuditing
@EnableAsync
public class FeedbackServiceApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FeedbackServiceApplication.class);
        application.run(args);
    }
}
