package com.gogidix.digitalmarketing.globalmarketingdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
    "com.gogidix.digitalmarketing.globalmarketingdashboard",
    "com.gogidix.digitalmarketing.shared"
})
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
public class GlobalMarketingDashboardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalMarketingDashboardServiceApplication.class, args);
    }
}
