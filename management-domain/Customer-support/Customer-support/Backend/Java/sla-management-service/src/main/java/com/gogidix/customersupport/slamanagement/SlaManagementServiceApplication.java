package com.gogidix.customersupport.slamanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = "com.gogidix.customersupport.slamanagement")
@EnableMongoAuditing
public class SlaManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SlaManagementServiceApplication.class);
        app.run(args);
    }
}
