package com.gogidix.shared.infrastructure.services.security.auth.application.service;

import com.gogidix.shared.infrastructure.services.security.auth.application.port.in.AuthenticationPort;
import com.gogidix.shared.infrastructure.services.security.auth.application.port.in.UserQueryPort;
import com.gogidix.shared.infrastructure.services.security.auth.domain.aggregate.AuthSessionRegistry;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for authentication operations.
 * Implements the hexagonal architecture pattern.
 */
@Service
public class AuthenticationService implements AuthenticationPort {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthSessionRegistry sessionRegistry;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(AuthSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Optional<AuthToken> login(String username, String password, String ipAddress) {
        log.info("Authentication attempt for user: {} from IP: {}", username, ipAddress);

        User user = sessionRegistry.findUserByUsername(username);
        if (user == null) {
            log.warn("Authentication failed: user not found - {}", username);
            return Optional.empty();
        }

        if (!user.isEnabled()) {
            log.warn("Authentication failed: user disabled - {}", username);
            return Optional.empty();
        }

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Authentication failed: invalid password for user - {}", username);
            return Optional.empty();
        }

        return sessionRegistry.authenticate(username, user.getPassword(), ipAddress);
    }

    @Override
    public boolean logout(String tokenId, String ipAddress) {
        log.info("Logout request for token: {} from IP: {}", tokenId, ipAddress);

        boolean result = sessionRegistry.logout(tokenId, ipAddress);

        if (result) {
            log.debug("Logout successful for token: {}", tokenId);
        } else {
            log.warn("Logout failed: token not found - {}", tokenId);
        }

        return result;
    }

    @Override
    public void logoutAll(String userId, String ipAddress) {
        log.info("Logout all sessions request for user: {} from IP: {}", userId, ipAddress);

        sessionRegistry.logoutAll(userId, ipAddress);

        log.debug("All sessions revoked for user: {}", userId);
    }

    @Override
    public Optional<AuthToken> refreshToken(String refreshToken) {
        log.debug("Token refresh request");

        return sessionRegistry.refreshToken(refreshToken);
    }

    @Override
    public boolean validateToken(String tokenId) {
        return sessionRegistry.validateToken(tokenId);
    }

    @Override
    public User register(String username, String email, String password, String tenantId) {
        log.info("User registration: username={}, email={}, tenantId={}", username, email, tenantId);

        // Check if user already exists
        if (sessionRegistry.findUserByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        if (sessionRegistry.findUserByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        String userId = UUID.randomUUID().toString();
        String passwordHash = passwordEncoder.encode(password);

        User user = sessionRegistry.registerUser(userId, tenantId, username, email, passwordHash);

        log.info("User registered successfully: userId={}, username={}", userId, username);

        return user;
    }
}
