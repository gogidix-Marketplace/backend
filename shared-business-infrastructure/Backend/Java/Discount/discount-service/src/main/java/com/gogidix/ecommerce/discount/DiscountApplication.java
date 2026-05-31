package com.gogidix.ecommerce.discount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.discount.domain.repository")
public class DiscountApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscountApplication.class, args);
    }
}
