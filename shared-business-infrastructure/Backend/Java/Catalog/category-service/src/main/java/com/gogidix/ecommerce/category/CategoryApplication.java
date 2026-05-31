package com.gogidix.ecommerce.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.gogidix.ecommerce.category")
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.category.domain.repository")
@EnableMongoAuditing
@EnableAsync
public class CategoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CategoryApplication.class, args);
    }
}
