package com.gogidix.shared.infrastructure.services.security.auth.application.service;

import com.gogidix.shared.infrastructure.services.security.auth.application.port.in.UserQueryPort;
import com.gogidix.shared.infrastructure.services.security.auth.domain.aggregate.AuthSessionRegistry;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service for user query operations.
 * Implements the hexagonal architecture pattern.
 */
@Service
public class UserQueryService implements UserQueryPort {

    private static final Logger log = LoggerFactory.getLogger(UserQueryService.class);

    private final AuthSessionRegistry sessionRegistry;

    public UserQueryService(AuthSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return sessionRegistry.getUser(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(sessionRegistry.findUserByUsername(username));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(sessionRegistry.findUserByEmail(email));
    }

    @Override
    public List<User> getAllUsers() {
        return sessionRegistry.getAllUsers();
    }

    @Override
    public List<User> getUsersByTenant(String tenantId) {
        return sessionRegistry.getUsersByTenant(tenantId);
    }

    @Override
    public List<AuthToken> getUserTokens(String userId) {
        return sessionRegistry.getUserTokens(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return sessionRegistry.findUserByUsername(username) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return sessionRegistry.findUserByEmail(email) != null;
    }
}
