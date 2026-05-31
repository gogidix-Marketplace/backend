package com.gogidix.shared.infrastructure.services.security.auth.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.LoginRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.RefreshTokenRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.AuthResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.UserResponseDto;

import java.util.Optional;

/**
 * Authentication use case port.
 * <p>
 * Defines the authentication operations available in the domain.
 */
public interface AuthPort {

    /**
     * Authenticates a user with username/email and password.
     *
     * @param request the login request
     * @return the authentication response with tokens
     */
    AuthResponseDto login(LoginRequestDto request);

    /**
     * Refreshes an authentication token using a refresh token.
     *
     * @param request the refresh token request
     * @return the new authentication response
     */
    AuthResponseDto refreshToken(RefreshTokenRequestDto request);

    /**
     * Logs out a user by revoking their tokens.
     *
     * @param token the token to revoke
     */
    void logout(String token);

    /**
     * Validates a token and returns the associated user if valid.
     *
     * @param token the token to validate
     * @return the user if the token is valid
     */
    Optional<UserResponseDto> validateToken(String token);

    /**
     * Finds a user by ID.
     *
     * @param userId the user ID
     * @return the user if found
     */
    Optional<UserResponseDto> findById(String userId);

    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return the user if found
     */
    Optional<UserResponseDto> findByUsername(String username);

    /**
     * Finds a user by email.
     *
     * @param email the email
     * @return the user if found
     */
    Optional<UserResponseDto> findByEmail(String email);
}
