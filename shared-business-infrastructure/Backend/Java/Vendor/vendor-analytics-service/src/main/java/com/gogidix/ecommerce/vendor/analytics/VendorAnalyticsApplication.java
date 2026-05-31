package com.gogidix.ecommerce.vendor.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class VendorAnalyticsApplication {
    public static void main(String[] args) { SpringApplication.run(VendorAnalyticsApplication.class, args); }
}