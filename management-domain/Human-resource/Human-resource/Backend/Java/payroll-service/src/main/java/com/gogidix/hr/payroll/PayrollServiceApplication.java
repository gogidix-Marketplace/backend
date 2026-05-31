package com.gogidix.hr.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Payroll Service Application
 * Main Spring Boot application class for managing payroll operations
 */
@SpringBootApplication
@EnableKafka
public class PayrollServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayrollServiceApplication.class, args);
    }
}
