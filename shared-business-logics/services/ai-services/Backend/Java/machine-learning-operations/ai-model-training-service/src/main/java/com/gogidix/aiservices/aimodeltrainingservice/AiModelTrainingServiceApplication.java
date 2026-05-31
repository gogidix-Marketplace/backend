package com.gogidix.aiservices.aimodeltrainingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for AI Model Training Service.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.aiservices.aimodeltrainingservice")
public class AiModelTrainingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiModelTrainingServiceApplication.class, args);
    }
}
