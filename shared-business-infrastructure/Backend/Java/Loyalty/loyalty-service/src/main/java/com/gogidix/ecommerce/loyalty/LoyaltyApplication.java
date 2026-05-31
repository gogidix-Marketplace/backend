package com.gogidix.ecommerce.loyalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.loyalty.domain.repository")
public class LoyaltyApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoyaltyApplication.class, args);
    }
}
