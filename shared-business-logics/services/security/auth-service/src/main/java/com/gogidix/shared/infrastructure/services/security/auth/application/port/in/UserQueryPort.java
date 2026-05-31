package com.gogidix.shared.infrastructure.services.security.auth.application.port.in;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Input port for user query operations.
 */
public interface UserQueryPort {

    /**
     * Get a user by ID.
     *
     * @param userId the user ID
     * @return the user if found
     */
    Optional<User> getUserById(String userId);

    /**
     * Find a user by username.
     *
     * @param username the username
     * @return the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by email.
     *
     * @param email the email
     * @return the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Get all users.
     *
     * @return list of all users
     */
    List<User> getAllUsers();

    /**
     * Get users by tenant.
     *
     * @param tenantId the tenant ID
     * @return list of users in the tenant
     */
    List<User> getUsersByTenant(String tenantId);

    /**
     * Get tokens for a user.
     *
     * @param userId the user ID
     * @return list of user's tokens
     */
    List<AuthToken> getUserTokens(String userId);

    /**
     * Check if a username exists.
     *
     * @param username the username
     * @return true if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if an email exists.
     *
     * @param email the email
     * @return true if email exists
     */
    boolean existsByEmail(String email);
}
