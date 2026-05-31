package com.gogidix.courier.routingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for Routing Service.
 * Handles route optimization and management for courier delivery operations.
 */
@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableAsync
@EnableKafka
public class RoutingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoutingServiceApplication.class, args);
    }
}
