package com.gogidix.ecommerce.oceanshipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.oceanshipping.domain.repository")
public class OceanShippingApplication {
    public static void main(String[] args) {
        SpringApplication.run(OceanShippingApplication.class, args);
    }
}
