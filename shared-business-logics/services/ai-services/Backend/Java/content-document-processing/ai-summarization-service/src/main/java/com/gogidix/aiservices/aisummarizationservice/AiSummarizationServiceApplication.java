package com.gogidix.aiservices.aisummarizationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiSummarizationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiSummarizationServiceApplication.class, args);
    }
}
