package com.gogidix.shared.courier.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * PricingEngineService Application
 *
 * Multi-tenant MongoDB service
 */
@SpringBootApplication
@EnableMongoAuditing
public class PricingEngineServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingEngineServiceApplication.class, args);
    }
}
