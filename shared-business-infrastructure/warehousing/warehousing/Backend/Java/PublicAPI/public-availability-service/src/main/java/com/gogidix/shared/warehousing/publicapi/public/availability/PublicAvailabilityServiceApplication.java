package com.gogidix.shared.warehousing.publicapi.availability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class PublicAvailabilityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicAvailabilityServiceApplication.class, args);
    }
}
