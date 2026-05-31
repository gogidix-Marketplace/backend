package com.gogidix.courier.assignmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for Assignment Service.
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class AssignmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentServiceApplication.class, args);
    }
}
