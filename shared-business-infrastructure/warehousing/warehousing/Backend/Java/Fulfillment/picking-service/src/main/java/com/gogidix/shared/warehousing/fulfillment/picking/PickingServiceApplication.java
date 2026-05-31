package com.gogidix.shared.warehousing.fulfillment.picking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * PickingService Application
 *
 * Multi-tenant picking service with MongoDB support
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class PickingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PickingServiceApplication.class, args);
    }
}
