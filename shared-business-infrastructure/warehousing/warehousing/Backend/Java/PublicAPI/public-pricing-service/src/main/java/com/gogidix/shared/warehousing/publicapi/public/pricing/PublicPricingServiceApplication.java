package com.gogidix.shared.warehousing.publicapi.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * PublicPricingService Application
 *
 * Multi-tenant public pricing service with MongoDB support
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class PublicPricingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicPricingServiceApplication.class, args);
    }
}
