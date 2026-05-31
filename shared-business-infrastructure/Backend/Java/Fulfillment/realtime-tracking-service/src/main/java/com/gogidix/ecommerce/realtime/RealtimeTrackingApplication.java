package com.gogidix.ecommerce.realtime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class RealtimeTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealtimeTrackingApplication.class, args);
    }
}
