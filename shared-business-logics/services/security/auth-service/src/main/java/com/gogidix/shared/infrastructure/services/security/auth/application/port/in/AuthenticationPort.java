package com.gogidix.shared.infrastructure.services.security.auth.application.port.in;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Input port for authentication operations.
 * Handles login, logout, token refresh, and user registration.
 */
public interface AuthenticationPort {

    /**
     * Authenticate a user with credentials.
     *
     * @param username the username
     * @param password the password
     * @param ipAddress the IP address of the request
     * @return the auth token if successful
     */
    Optional<AuthToken> login(String username, String password, String ipAddress);

    /**
     * Logout a user and invalidate their token.
     *
     * @param tokenId the token ID
     * @param ipAddress the IP address of the request
     * @return true if logout was successful
     */
    boolean logout(String tokenId, String ipAddress);

    /**
     * Logout all sessions for a user.
     *
     * @param userId the user ID
     * @param ipAddress the IP address of the request
     */
    void logoutAll(String userId, String ipAddress);

    /**
     * Refresh an authentication token.
     *
     * @param refreshToken the refresh token
     * @return the new auth token if successful
     */
    Optional<AuthToken> refreshToken(String refreshToken);

    /**
     * Validate a token.
     *
     * @param tokenId the token ID
     * @return true if token is valid
     */
    boolean validateToken(String tokenId);

    /**
     * Register a new user.
     *
     * @param username the username
     * @param email the email
     * @param password the password
     * @param tenantId the tenant ID
     * @return the created user
     */
    User register(String username, String email, String password, String tenantId);
}
