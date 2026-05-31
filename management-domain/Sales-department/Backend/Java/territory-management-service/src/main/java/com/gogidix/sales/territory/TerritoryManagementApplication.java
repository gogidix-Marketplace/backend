package com.gogidix.sales.territory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Territory Management Service Application
 * Multi-tenant SaaS hexagonal architecture for Sales Department
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class TerritoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerritoryManagementApplication.class, args);
    }
}
