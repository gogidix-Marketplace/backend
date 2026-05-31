package com.gogidix.digitalmarketing.brandmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Brand Management Service Application
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.digitalmarketing.brandmanagement",
    "com.gogidix.digitalmarketing.shared"
})
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
public class BrandManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrandManagementServiceApplication.class, args);
    }
}
