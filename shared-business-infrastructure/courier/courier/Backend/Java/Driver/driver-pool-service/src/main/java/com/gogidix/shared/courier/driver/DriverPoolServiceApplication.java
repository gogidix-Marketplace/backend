package com.gogidix.shared.courier.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * DriverPoolService Application
 *
 * Multi-tenant MongoDB service with Geospatial Support
 */
@SpringBootApplication
@EnableMongoAuditing
public class DriverPoolServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverPoolServiceApplication.class, args);
    }
}
