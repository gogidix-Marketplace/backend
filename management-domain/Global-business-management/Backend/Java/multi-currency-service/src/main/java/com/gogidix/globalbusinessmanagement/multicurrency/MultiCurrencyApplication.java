package com.gogidix.globalbusinessmanagement.multicurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main application class for Multi-Currency Service.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.globalbusinessmanagement")
@EnableMongoAuditing
@EnableCaching
public class MultiCurrencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiCurrencyApplication.class, args);
    }
}
