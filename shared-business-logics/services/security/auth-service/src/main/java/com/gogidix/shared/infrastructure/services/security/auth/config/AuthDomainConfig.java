package com.gogidix.shared.infrastructure.services.security.auth.config;

import com.gogidix.shared.infrastructure.services.security.auth.application.service.AuthenticationService;
import com.gogidix.shared.infrastructure.services.security.auth.application.service.UserQueryService;
import com.gogidix.shared.infrastructure.services.security.auth.domain.aggregate.AuthSessionRegistry;
import com.gogidix.shared.infrastructure.services.security.auth.domain.event.UserLoggedInEvent;
import com.gogidix.shared.infrastructure.services.security.auth.domain.event.UserLoggedOutEvent;
import com.gogidix.shared.infrastructure.services.security.auth.domain.event.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for authentication domain layer.
 * Creates aggregate roots and wires up event handlers.
 */
@Configuration
public class AuthDomainConfig {

    private static final Logger log = LoggerFactory.getLogger(AuthDomainConfig.class);

    @Bean
    public AuthSessionRegistry authSessionRegistry() {
        AuthSessionRegistry registry = new AuthSessionRegistry();

        // Register event handlers
        registry.onUserLoggedIn(this::handleUserLoggedIn);
        registry.onUserLoggedOut(this::handleUserLoggedOut);
        registry.onUserRegistered(this::handleUserRegistered);

        log.info("AuthSessionRegistry aggregate initialized with event handlers");

        return registry;
    }

    private void handleUserLoggedIn(UserLoggedInEvent event) {
        log.info("User logged in: userId={}, username={}, ip={}",
                event.getUserId(), event.getUsername(), event.getIpAddress());
    }

    private void handleUserLoggedOut(UserLoggedOutEvent event) {
        log.info("User logged out: userId={}, ip={}", event.getUserId(), event.getIpAddress());
    }

    private void handleUserRegistered(UserRegisteredEvent event) {
        log.info("User registered: userId={}, username={}, email={}, tenantId={}",
                event.getUserId(), event.getUsername(), event.getEmail(), event.getTenantId());
    }
}
