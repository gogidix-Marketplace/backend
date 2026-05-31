package com.gogidix.shared.warehousing.publicapi.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * PublicBookingService Application
 *
 * Multi-tenant public booking service with MongoDB support
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class PublicBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicBookingServiceApplication.class, args);
    }
}
