package com.gogidix.globalbusinessmanagement.datavalidation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Data Validation Service
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.globalbusiness.datavalidation")
@EnableCaching
@EnableAsync
@EnableScheduling
public class DataValidationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataValidationServiceApplication.class, args);
    }
}
