package com.gogidix.shared.warehousing.availability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Availability Service Application
 */
@EnableKafka
@SpringBootApplication(scanBasePackages = "com.gogidix.shared.warehousing.availability")
public class AvailabilityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvailabilityServiceApplication.class, args);
    }
}
