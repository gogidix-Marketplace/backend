package com.gogidix.customersupport.ticketmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = "com.gogidix.customersupport.ticketmanagement")
@EnableMongoAuditing
public class TicketManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TicketManagementServiceApplication.class);
        app.run(args);
    }
}
