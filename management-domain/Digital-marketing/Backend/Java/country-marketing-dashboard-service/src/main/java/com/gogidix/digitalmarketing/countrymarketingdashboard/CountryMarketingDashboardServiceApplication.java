package com.gogidix.digitalmarketing.countrymarketingdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
    "com.gogidix.digitalmarketing.countrymarketingdashboard",
    "com.gogidix.digitalmarketing.shared"
})
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
public class CountryMarketingDashboardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CountryMarketingDashboardServiceApplication.class, args);
    }
}
