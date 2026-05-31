package com.gogidix.customersupport.customerportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = "com.gogidix.customersupport.customerportal")
@EnableMongoAuditing
public class CustomerPortalServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CustomerPortalServiceApplication.class);
        app.run(args);
    }
}
