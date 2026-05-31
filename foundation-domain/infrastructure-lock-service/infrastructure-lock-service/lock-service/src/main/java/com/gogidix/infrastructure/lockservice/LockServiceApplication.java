package com.gogidix.infrastructure.lockservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Infrastructure Lock Service.
 * Provides distributed locking capabilities with Redis backend.
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class LockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LockServiceApplication.class, args);
    }
}
