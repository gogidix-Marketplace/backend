package com.gogidix.hr.globalhrdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Application class for Global HR Dashboard Service
 *
 * This service provides global human resource analytics and oversight across all regions,
 * including headcount tracking, compliance monitoring, diversity metrics, retention
 * analytics, and performance management capabilities.
 */
@SpringBootApplication
@EnableKafka
@EnableAsync
public class GlobalHRDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalHRDashboardApplication.class, args);
    }
}
