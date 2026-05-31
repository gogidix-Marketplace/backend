package com.gogidix.ecommerce.pushnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.pushnotification.infrastructure.persistence.repository")
public class PushNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(PushNotificationApplication.class, args);
    }
}