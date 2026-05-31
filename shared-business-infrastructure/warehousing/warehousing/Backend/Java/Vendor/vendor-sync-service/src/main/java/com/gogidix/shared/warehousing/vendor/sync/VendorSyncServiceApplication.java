package com.gogidix.shared.warehousing.vendor.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * VendorSyncService Application
 *
 * Multi-tenant vendor sync service with MongoDB support
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class VendorSyncServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendorSyncServiceApplication.class, args);
    }
}
