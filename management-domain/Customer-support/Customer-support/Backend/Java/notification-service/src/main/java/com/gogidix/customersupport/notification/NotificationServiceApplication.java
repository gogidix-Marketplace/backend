package com.gogidix.customersupport.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.gogidix.customersupport.notification")
@EnableMongoAuditing
@EnableAsync
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NotificationServiceApplication.class);
        app.run(args);
    }
}
