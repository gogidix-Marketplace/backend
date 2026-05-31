package com.gogidix.aiservices.aiinferenceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for AI Inference Service.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.aiservices.aiinferenceservice")
public class AiInferenceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiInferenceServiceApplication.class, args);
    }
}
