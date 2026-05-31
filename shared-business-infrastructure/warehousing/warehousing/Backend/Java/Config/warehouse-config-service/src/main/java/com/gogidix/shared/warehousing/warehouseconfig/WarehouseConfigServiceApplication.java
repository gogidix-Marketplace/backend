package com.gogidix.shared.warehousing.warehouseconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Warehouse Config Service Application
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.shared.warehousing.warehouseconfig")
public class WarehouseConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseConfigServiceApplication.class, args);
    }
}
