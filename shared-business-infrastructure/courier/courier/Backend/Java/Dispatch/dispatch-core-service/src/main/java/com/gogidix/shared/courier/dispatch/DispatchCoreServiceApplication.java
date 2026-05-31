package com.gogidix.shared.courier.dispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * DispatchCoreService Application
 *
 * Multi-tenant dispatch service with MongoDB and geospatial support
 */
@SpringBootApplication
@EnableMongoAuditing
public class DispatchCoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DispatchCoreServiceApplication.class, args);
    }
}
