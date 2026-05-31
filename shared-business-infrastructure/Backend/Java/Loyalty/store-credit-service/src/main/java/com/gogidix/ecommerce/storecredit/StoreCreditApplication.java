package com.gogidix.ecommerce.storecredit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.storecredit.domain.repository")
public class StoreCreditApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreCreditApplication.class, args);
    }
}
