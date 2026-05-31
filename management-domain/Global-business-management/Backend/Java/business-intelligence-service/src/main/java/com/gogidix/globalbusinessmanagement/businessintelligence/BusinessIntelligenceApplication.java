package com.gogidix.globalbusinessmanagement.businessintelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main application class for Business Intelligence Service.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.globalbusinessmanagement")
@EnableMongoAuditing
@EnableCaching
public class BusinessIntelligenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessIntelligenceApplication.class, args);
    }
}
