package com.gogidix.ecommerce.communication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.communication.domain.repository")
public class CommunicationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunicationApplication.class, args);
    }
}
