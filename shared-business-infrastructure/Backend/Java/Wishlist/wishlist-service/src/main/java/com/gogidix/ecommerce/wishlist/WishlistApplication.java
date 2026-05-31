package com.gogidix.ecommerce.wishlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.wishlist.domain.repository")
public class WishlistApplication {
    public static void main(String[] args) {
        SpringApplication.run(WishlistApplication.class, args);
    }
}
