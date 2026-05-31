package com.gogidix.ecommerce.promotion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.promotion.domain.repository")
public class PromotionApplication {
    public static void main(String[] args) {
        SpringApplication.run(PromotionApplication.class, args);
    }
}
