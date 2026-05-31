package com.gogidix.centralconfiguration.configauditservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Config Audit Service.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class ConfigAuditServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigAuditServiceApplication.class, args);
    }
}
