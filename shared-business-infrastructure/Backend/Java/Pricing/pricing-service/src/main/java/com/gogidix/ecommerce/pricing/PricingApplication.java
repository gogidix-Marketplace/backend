package com.gogidix.ecommerce.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.gogidix.ecommerce.pricing")
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.pricing.domain.repository")
@EnableMongoAuditing
@EnableAsync
public class PricingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingApplication.class, args);
    }
}
