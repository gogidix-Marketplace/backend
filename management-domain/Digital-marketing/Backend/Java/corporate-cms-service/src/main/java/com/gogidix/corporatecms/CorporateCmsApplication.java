package com.gogidix.corporatecms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Corporate CMS Service.
 * This is a headless CMS backend for managing corporate website content.
 *
 * Features:
 * - Content CRUD operations (Pages, Blog, Press Releases, Resources)
 * - Media management (images, videos, documents)
 * - Workflow approval system
 * - Role-based access control (ADMIN, CONTENT_EDITOR, PRODUCT_MANAGER, HR_MANAGER, PR_MANAGER)
 * - Version control for content
 * - Publishing/scheduling
 * - Product catalog management
 * - Developer resources management
 * - Careers management
 * - Lead management integration
 * - Analytics dashboard data
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.corporatecms")
@EnableMongoAuditing
@EnableCaching
@EnableAsync
@EnableScheduling
public class CorporateCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorporateCmsApplication.class, args);
    }
}
