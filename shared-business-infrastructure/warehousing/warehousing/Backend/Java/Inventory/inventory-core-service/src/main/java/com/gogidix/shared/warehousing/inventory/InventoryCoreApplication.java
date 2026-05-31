package com.gogidix.shared.warehousing.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Inventory Core Service Application
 *
 * Multi-tenant inventory management with MongoDB
 * Supports: E-commerce vendors, Logistics, Procurement, Public marketplace
 */
@SpringBootApplication
public class InventoryCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryCoreApplication.class, args);
    }
}

