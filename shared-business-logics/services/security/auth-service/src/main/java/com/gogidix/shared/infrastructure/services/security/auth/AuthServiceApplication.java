package com.gogidix.shared.infrastructure.services.security.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Authentication Service Application.
 * <p>
 * Main Spring Boot application for the authentication service.
 * Provides JWT/OAuth2 authentication with multi-tenant support.
 */
@SpringBootApplication(scanBasePackages = {
        "com.gogidix.shared.infrastructure.services.security.auth",
        "com.gogidix.shared.servicediscovery.config",
        "com.gogidix.shared"
})
@EnableMongoAuditing
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
