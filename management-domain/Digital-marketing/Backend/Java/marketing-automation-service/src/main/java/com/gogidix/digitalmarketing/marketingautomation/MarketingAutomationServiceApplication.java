package com.gogidix.digitalmarketing.marketingautomation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
    "com.gogidix.digitalmarketing.marketingautomation",
    "com.gogidix.digitalmarketing.shared"
})
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
public class MarketingAutomationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketingAutomationServiceApplication.class, args);
    }
}
