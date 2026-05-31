package com.gogidix.customersupport.supportanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = "com.gogidix.customersupport.supportanalytics")
@EnableMongoAuditing
public class SupportAnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SupportAnalyticsServiceApplication.class);
        app.run(args);
    }
}
