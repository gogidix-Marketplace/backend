package com.gogidix.digitalmarketing.socialmedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Social Media Service Application
 *
 * <p>This service manages social media posts, accounts, and engagement across platforms.
 * Supports multi-platform posting, content scheduling, and engagement tracking.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Multi-platform account management (Facebook, Twitter, Instagram, LinkedIn, TikTok)</li>
 *   <li>Content scheduling and publishing</li>
 *   <li>Content library with reusable templates</li>
 *   <li>Engagement metrics tracking</li>
 *   <li>Content calendar view</li>
 *   <li>Tenant-isolated data</li>
 * </ul>
 *
 * @author Gogidix
 * @version 1.0.0
 */
@SpringBootApplication(
    scanBasePackages = {
        "com.gogidix.digitalmarketing.socialmedia"
    }
)
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.gogidix.digitalmarketing.socialmedia")
@EnableCaching
@EnableAsync
@EnableScheduling
public class SocialMediaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialMediaServiceApplication.class, args);
    }
}
