package com.gogidix.shared.infrastructure.services.observability.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = {
        "com.gogidix.shared.infrastructure.services.observability.audit",
        "com.gogidix.shared.infrastructure.core.tenancy",
        "com.gogidix.shared"
})
@EnableMongoAuditing
public class AuditServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditServiceApplication.class, args);
    }
}
