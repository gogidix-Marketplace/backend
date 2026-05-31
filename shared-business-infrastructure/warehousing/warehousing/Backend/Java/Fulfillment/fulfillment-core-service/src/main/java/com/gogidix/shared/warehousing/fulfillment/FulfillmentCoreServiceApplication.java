package com.gogidix.shared.warehousing.fulfillment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * FulfillmentCoreService Application
 *
 * Multi-tenant fulfillment service with MongoDB support
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class FulfillmentCoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FulfillmentCoreServiceApplication.class, args);
    }
}
