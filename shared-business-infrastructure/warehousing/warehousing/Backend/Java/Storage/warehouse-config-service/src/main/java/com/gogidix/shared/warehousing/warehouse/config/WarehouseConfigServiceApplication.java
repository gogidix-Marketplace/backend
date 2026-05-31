package com.gogidix.shared.warehousing.warehouse.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Warehouse Config Service Application
 *
 * Multi-tenant MongoDB service for warehouse configuration management
 */
@SpringBootApplication
@EnableMongoAuditing
public class WarehouseConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseConfigServiceApplication.class, args);
    }
}
