package com.gogidix.courier.performanceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Main application class for Driver Performance Service.
 */
@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableAsync
@EnableKafka
public class PerformanceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformanceServiceApplication.class, args);
    }
}
