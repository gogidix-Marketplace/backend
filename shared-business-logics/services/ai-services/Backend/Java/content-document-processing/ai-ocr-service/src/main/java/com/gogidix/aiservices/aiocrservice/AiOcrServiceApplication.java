package com.gogidix.aiservices.aiocrservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiOcrServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiOcrServiceApplication.class, args);
    }
}
