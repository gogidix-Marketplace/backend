package com.gogidix.shared.infrastructure.services.infrastructure.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Caching Service Application.
 * Provides distributed caching capabilities using Redis.
 */
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.gogidix.shared.infrastructure.services.infrastructure.cache")
public class CachingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CachingServiceApplication.class, args);
    }
}
