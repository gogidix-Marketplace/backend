package com.gogidix.shared.warehousing.inventory.selfstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * SelfStorageService Application
 *
 * Multi-tenant self storage service with MongoDB support
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class SelfStorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelfStorageServiceApplication.class, args);
    }
}
