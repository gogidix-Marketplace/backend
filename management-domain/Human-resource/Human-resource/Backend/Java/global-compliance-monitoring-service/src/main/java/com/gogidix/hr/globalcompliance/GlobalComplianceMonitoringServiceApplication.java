package com.gogidix.hr.globalcompliance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Global Compliance Monitoring Service Application
 * Main Spring Boot application class
 */
@SpringBootApplication
@EnableKafka
public class GlobalComplianceMonitoringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalComplianceMonitoringServiceApplication.class, args);
    }
}
