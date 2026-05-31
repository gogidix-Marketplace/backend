package com.gogidix.ecommerce.giftcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.giftcard.domain.repository")
public class GiftCardApplication {
    public static void main(String[] args) {
        SpringApplication.run(GiftCardApplication.class, args);
    }
}
