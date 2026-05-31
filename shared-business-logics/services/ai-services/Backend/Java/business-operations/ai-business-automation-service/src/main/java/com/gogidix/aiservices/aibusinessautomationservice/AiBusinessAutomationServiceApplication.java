package com.gogidix.aiservices.aibusinessautomationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiBusinessAutomationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiBusinessAutomationServiceApplication.class, args);
    }
}
