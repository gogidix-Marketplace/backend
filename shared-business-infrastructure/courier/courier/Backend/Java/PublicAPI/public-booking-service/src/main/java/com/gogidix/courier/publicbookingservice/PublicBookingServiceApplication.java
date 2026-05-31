package com.gogidix.courier.publicbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableKafka
public class PublicBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicBookingServiceApplication.class, args);
    }
}
