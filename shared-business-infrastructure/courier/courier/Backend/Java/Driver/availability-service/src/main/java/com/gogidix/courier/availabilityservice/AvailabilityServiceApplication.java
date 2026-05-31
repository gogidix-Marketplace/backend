package com.gogidix.courier.availabilityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Main application class for Driver Availability Service.
 */
@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableAsync
@EnableKafka
public class AvailabilityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvailabilityServiceApplication.class, args);
    }
}
