package com.gogidix.monitoring.monitoringdataservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Monitoring Data Service.
 * This service is responsible for collecting, storing, and aggregating
 * metrics from all 53 services in the Gogidix ecosystem.
 */
@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableKafka
@EnableScheduling
public class MonitoringDataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringDataServiceApplication.class, args);
    }
}
