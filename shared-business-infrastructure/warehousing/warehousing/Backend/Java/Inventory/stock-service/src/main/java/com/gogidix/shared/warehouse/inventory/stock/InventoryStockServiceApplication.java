package com.gogidix.shared.warehouse.inventory.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main Application class for Inventory Stock Service
 * Manages stock levels and inventory movements
 */
@SpringBootApplication
@EnableKafka
@EnableMongoRepositories
public class InventoryStockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryStockServiceApplication.class, args);
    }
}
