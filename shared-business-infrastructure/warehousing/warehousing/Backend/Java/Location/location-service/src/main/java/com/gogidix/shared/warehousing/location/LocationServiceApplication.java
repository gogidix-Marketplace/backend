package com.gogidix.shared.warehousing.location;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * LocationService Application
 *
 * Multi-tenant location service with MongoDB support
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class LocationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationServiceApplication.class, args);
    }
}
