package com.gogidix.shared.warehousing.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpaceService Application
 *
 * Multi-tenant storage space allocation service
 */
@SpringBootApplication
public class SpaceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpaceServiceApplication.class, args);
    }
}
