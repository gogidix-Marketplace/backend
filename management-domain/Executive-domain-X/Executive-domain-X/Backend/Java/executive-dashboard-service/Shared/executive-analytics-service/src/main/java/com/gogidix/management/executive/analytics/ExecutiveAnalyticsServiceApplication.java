package com.gogidix.management.executive.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(
    scanBasePackages = {
        "com.gogidix.management.executive.analytics",
        "com.gogidix.management.shared"
    }
)
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.gogidix.management")
@EnableCaching
@EnableAsync
public class ExecutiveAnalyticsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExecutiveAnalyticsServiceApplication.class, args);
    }
}
