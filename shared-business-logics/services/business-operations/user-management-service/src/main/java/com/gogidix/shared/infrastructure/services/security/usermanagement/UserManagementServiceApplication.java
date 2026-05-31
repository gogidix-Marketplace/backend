package com.gogidix.shared.infrastructure.services.security.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = {
        "com.gogidix.shared.infrastructure.services.security.usermanagement",
        "com.gogidix.shared.servicediscovery.config",
        "com.gogidix.shared"
})
@EnableMongoAuditing
public class UserManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementServiceApplication.class, args);
    }
}
