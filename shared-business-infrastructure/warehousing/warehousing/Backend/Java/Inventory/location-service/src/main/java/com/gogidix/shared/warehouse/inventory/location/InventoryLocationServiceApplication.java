package com.gogidix.shared.warehouse.inventory.location;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main Application class for Inventory Location Service
 * Manages storage locations within warehouse inventory system
 */
@SpringBootApplication
@EnableKafka
@EnableMongoRepositories
public class InventoryLocationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryLocationServiceApplication.class, args);
    }
}
