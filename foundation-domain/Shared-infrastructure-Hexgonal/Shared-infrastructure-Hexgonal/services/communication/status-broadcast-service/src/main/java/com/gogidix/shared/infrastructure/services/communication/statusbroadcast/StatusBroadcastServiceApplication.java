package com.gogidix.shared.infrastructure.services.communication.statusbroadcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main application class for Status Broadcast Service
 * Hexagonal Architecture implementation with multi-tenant support
 */
@SpringBootApplication(scanBasePackages = {
        "com.gogidix.shared.infrastructure.services.communication.statusbroadcast",
        "com.gogidix.shared.infrastructure.core.tenancy",
        "com.gogidix.shared"
})
@EnableMongoAuditing
public class StatusBroadcastServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatusBroadcastServiceApplication.class, args);
    }
}
