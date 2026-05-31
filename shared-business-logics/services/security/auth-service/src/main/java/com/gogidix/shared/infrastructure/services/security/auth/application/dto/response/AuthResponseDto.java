package com.gogidix.shared.infrastructure.services.security.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

/**
 * Authentication response DTO.
 * <p>
 * Contains the access token, refresh token, and user information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    /**
     * JWT access token.
     */
    private String accessToken;

    /**
     * Refresh token for obtaining new access tokens.
     */
    private String refreshToken;

    /**
     * Token type (e.g., "Bearer").
     */
    private String tokenType;

    /**
     * Access token expiration time.
     */
    private Instant expiresAt;

    /**
     * Refresh token expiration time.
     */
    private Instant refreshExpiresAt;

    /**
     * Authenticated user information.
     */
    private UserResponseDto user;
}
