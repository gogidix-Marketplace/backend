package com.gogidix.ecommerce.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class MarketplaceSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceSearchApplication.class, args);
    }
}
