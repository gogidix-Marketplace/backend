package com.gogidix.sales.forecast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Forecast Management Service Application
 * Multi-tenant SaaS hexagonal architecture for Sales Department
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class ForecastManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForecastManagementApplication.class, args);
    }
}
