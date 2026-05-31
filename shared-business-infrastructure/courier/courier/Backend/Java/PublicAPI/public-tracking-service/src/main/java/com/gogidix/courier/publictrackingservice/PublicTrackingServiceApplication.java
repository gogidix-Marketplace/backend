package com.gogidix.courier.publictrackingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableKafka
public class PublicTrackingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicTrackingServiceApplication.class, args);
    }
}
