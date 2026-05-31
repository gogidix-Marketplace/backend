package com.gogidix.ecommerce.reward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.reward.domain.repository")
public class RewardApplication {
    public static void main(String[] args) {
        SpringApplication.run(RewardApplication.class, args);
    }
}
