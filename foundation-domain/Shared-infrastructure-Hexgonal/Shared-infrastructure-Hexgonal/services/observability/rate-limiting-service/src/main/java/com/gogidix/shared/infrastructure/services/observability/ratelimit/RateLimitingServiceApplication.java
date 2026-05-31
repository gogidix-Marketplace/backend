package com.gogidix.shared.infrastructure.services.observability.ratelimit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = {
        "com.gogidix.shared.infrastructure.services.observability.ratelimit",
        "com.gogidix.shared.infrastructure.core.tenancy",
        "com.gogidix.shared"
})
@EnableMongoAuditing
public class RateLimitingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateLimitingServiceApplication.class, args);
    }
}
