package com.gogidix.courier.gpstrackingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for GPS Tracking Service.
 * Provides real-time GPS tracking and monitoring for courier drivers.
 */
@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableAsync
@EnableScheduling
public class GpsTrackingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpsTrackingServiceApplication.class, args);
    }
}
