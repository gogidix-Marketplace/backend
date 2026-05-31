package com.gogidix.shared.infrastructure.services.security.tenantmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = {
        "com.gogidix.shared.infrastructure.services.security.tenantmanagement",
        "com.gogidix.shared.multitenancy",
        "com.gogidix.shared"
})
@EnableMongoAuditing
public class TenantManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenantManagementServiceApplication.class, args);
    }
}