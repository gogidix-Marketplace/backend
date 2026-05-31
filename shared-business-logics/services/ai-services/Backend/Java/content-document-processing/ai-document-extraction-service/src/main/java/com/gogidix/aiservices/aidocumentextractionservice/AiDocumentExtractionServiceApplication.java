package com.gogidix.aiservices.aidocumentextractionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiDocumentExtractionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiDocumentExtractionServiceApplication.class, args);
    }
}
