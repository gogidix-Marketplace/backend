package com.gogidix.marketing.campaign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Campaign Management Service Application
 *
 * <p>Main Spring Boot application class for the Campaign Management Service.</p>
 *
 * <p>This service manages marketing campaigns with multi-channel support,
 * budget tracking, and performance monitoring.</p>
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.marketing.campaign",
    "com.gogidix.digitalmarketing.campaignmanagement"
})
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
public class CampaignManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampaignManagementServiceApplication.class, args);
    }
}
