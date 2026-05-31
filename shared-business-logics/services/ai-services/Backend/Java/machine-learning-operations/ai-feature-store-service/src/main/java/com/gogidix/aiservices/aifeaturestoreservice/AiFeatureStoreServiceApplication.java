package com.gogidix.aiservices.aifeaturestoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for AI Feature Store Service.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.aiservices.aifeaturestoreservice")
public class AiFeatureStoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiFeatureStoreServiceApplication.class, args);
    }
}
