package com.gogidix.ecommerce.airfreight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.airfreight.domain.repository")
public class AirFreightApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirFreightApplication.class, args);
    }
}
