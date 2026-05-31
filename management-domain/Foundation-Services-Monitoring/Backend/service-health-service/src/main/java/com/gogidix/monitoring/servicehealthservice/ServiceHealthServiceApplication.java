package com.gogidix.monitoring.servicehealthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Service Health Service.
 * This service is responsible for aggregating health status from all services,
 * tracking uptime/downtime, service dependency mapping, and health score calculation.
 */
@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableKafka
@EnableScheduling
public class ServiceHealthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHealthServiceApplication.class, args);
    }
}
