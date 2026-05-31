package com.gogidix.shared.warehousing.fulfillment.packing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * PackingService Application
 *
 * Multi-tenant packing service with MongoDB support
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class PackingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PackingServiceApplication.class, args);
    }
}
