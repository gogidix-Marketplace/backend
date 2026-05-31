package com.gogidix.digitalmarketing.seo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SEO Service Application
 *
 * <p>Main Spring Boot application class for the SEO Service.</p>
 *
 * <p>This service manages SEO optimization, keyword tracking,
 * and search engine performance monitoring.</p>
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.digitalmarketing.seo",
    "com.gogidix.digitalmarketing.shared"
})
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
public class SeoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeoServiceApplication.class, args);
    }
}
