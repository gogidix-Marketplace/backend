package com.gogidix.aiservices.aitrainingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for AI Training Service.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.aiservices.aitrainingservice")
public class AiTrainingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiTrainingServiceApplication.class, args);
    }
}
