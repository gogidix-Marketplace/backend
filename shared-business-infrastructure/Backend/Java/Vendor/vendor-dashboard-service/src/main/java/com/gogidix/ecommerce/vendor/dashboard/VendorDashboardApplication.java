package com.gogidix.ecommerce.vendor.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class VendorDashboardApplication {
    public static void main(String[] args) { SpringApplication.run(VendorDashboardApplication.class, args); }
}