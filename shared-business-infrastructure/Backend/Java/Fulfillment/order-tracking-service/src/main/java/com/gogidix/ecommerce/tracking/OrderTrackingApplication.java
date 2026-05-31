package com.gogidix.ecommerce.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class OrderTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderTrackingApplication.class, args);
    }
}
