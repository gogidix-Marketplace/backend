package com.gogidix.shared.courier.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * NotificationService Application
 *
 * Multi-tenant service with MongoDB
 */
@SpringBootApplication
@EnableMongoAuditing
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
