package com.gogidix.monitoring.alertmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Alert Management Service.
 * This service is responsible for alert rule configuration, alert generation
 * based on thresholds, alert history and acknowledgment, and notification routing.
 */
@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableKafka
@EnableScheduling
public class AlertManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertManagementServiceApplication.class, args);
    }
}
