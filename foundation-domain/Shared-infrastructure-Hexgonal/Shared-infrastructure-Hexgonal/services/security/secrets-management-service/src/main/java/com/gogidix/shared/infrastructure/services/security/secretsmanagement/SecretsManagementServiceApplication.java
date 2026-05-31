package com.gogidix.shared.infrastructure.services.security.secretsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.gogidix.shared.infrastructure.services.security.secretsmanagement")
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
@EnableCaching
public class SecretsManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecretsManagementServiceApplication.class, args);
    }
}
