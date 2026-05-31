package com.gogidix.platform.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Subscription Service Application
 *
 * Subscription lifecycle and billing management service providing:
 * - Subscription plan management
 * - Subscription lifecycle (create, upgrade, cancel, renew)
 * - Invoice generation and management
 * - Payment processing (Stripe integration)
 * - Usage-based billing
 * - Trial management
 *
 * @author Gogidix Platform Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableScheduling
public class SubscriptionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionServiceApplication.class, args);
    }
}
