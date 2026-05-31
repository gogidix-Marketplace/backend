package com.gogidix.digitalmarketing.contentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Content Management Service Application
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.digitalmarketing.contentmanagement",
    "com.gogidix.digitalmarketing.shared"
})
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
public class ContentManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentManagementServiceApplication.class, args);
    }
}
