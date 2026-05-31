package com.gogidix.hr.leavemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Leave Management Service Application
 * Main Spring Boot application class for managing leave operations
 */
@SpringBootApplication
@EnableKafka
public class LeaveManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaveManagementServiceApplication.class, args);
    }
}
