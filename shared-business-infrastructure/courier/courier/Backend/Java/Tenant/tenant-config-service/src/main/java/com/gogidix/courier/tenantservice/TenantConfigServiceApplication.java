package com.gogidix.courier.tenantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for Tenant Configuration Service.
 */
@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableAsync
public class TenantConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenantConfigServiceApplication.class, args);
    }
}
