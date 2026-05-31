package com.gogidix.shared.warehousing.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Pricing Service Application
 *
 * Main Spring Boot application for pricing service
 * Provides storage pricing engine with multi-tenant support
 */
@EnableKafka
@SpringBootApplication(scanBasePackages = "com.gogidix.shared.warehousing.pricing")
public class PricingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingServiceApplication.class, args);
    }
}
