package com.gogidix.aiservices.aimanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for AI Model Management Service.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.aiservices.aimanagementservice")
public class AiModelManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiModelManagementServiceApplication.class, args);
    }
}
