package com.gogidix.aiservices.aifeatureextractionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for AI Feature Extraction Service.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.aiservices.aifeatureextractionservice")
public class AiFeatureExtractionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiFeatureExtractionServiceApplication.class, args);
    }
}
