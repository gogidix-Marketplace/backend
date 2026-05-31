package com.gogidix.globalbusinessmanagement.regionalaggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main application class for Regional Aggregation Service.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.globalbusinessmanagement")
@EnableMongoAuditing
@EnableCaching
public class RegionalAggregationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegionalAggregationApplication.class, args);
    }
}
