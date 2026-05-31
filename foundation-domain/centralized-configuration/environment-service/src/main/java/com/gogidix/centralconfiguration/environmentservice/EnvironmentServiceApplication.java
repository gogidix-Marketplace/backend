package com.gogidix.centralconfiguration.environmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Environment Service.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class EnvironmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnvironmentServiceApplication.class, args);
    }
}
