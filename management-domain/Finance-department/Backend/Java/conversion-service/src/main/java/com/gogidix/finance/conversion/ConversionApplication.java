package com.gogidix.finance.conversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Currency Conversion Service Application
 * Multi-tenant SaaS hexagonal architecture for Finance Department
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
@EnableCaching
@EnableScheduling
public class ConversionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConversionApplication.class, args);
    }
}
