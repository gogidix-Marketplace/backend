package com.gogidix.aiservices.aidocumentclassificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiDocumentClassificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiDocumentClassificationServiceApplication.class, args);
    }
}
