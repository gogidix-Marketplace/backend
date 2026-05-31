package com.gogidix.globalbusiness.export;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main application class for Export Service.
 * Handles data export in various formats (CSV, Excel, PDF, JSON).
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.globalbusiness.export"
})
@EnableMongoAuditing
public class ExportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExportServiceApplication.class, args);
    }
}
