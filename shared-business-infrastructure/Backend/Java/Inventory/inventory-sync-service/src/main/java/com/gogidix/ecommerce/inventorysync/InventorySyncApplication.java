package com.gogidix.ecommerce.inventorysync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.inventorysync.domain.repository")
public class InventorySyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventorySyncApplication.class, args);
    }
}
