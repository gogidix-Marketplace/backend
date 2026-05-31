package com.gogidix.shared.infrastructure.services.apimanagement.ratelimit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ApiRateLimitServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiRateLimitServiceApplication.class, args);
    }
}
