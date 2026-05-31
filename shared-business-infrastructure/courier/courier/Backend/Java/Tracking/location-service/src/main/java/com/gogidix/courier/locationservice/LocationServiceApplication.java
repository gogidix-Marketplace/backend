package com.gogidix.courier.locationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Main application class for Location Service.
 */
@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableAsync
@EnableKafka
public class LocationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationServiceApplication.class, args);
    }
}
