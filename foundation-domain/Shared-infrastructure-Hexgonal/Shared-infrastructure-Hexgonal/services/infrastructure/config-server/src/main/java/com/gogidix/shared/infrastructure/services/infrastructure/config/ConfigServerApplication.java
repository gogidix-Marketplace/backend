package com.gogidix.shared.infrastructure.services.infrastructure.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Config Server Application.
 * Centralized configuration management service using Spring Cloud Config.
 */
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.gogidix.shared.infrastructure.services.infrastructure.config")
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
