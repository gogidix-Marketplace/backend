package com.gogidix.shared.infrastructure.services.security.auth.domain.port.out;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;

import java.util.Optional;

/**
 * Token repository output port.
 * <p>
 * Defines the contract for token persistence operations.
 */
public interface TokenRepositoryPort {

    /**
     * Saves a token.
     *
     * @param token the token to save
     * @return the saved token
     */
    AuthToken save(AuthToken token);

    /**
     * Finds a token by its value.
     *
     * @param token the token value
     * @return the token if found
     */
    Optional<AuthToken> findByToken(String token);

    /**
     * Finds a token by refresh token value.
     *
     * @param refreshToken the refresh token value
     * @return the token if found
     */
    Optional<AuthToken> findByRefreshToken(String refreshToken);

    /**
     * Finds a valid token by its value.
     *
     * @param token the token value
     * @return the token if found and valid
     */
    Optional<AuthToken> findValidByToken(String token);

    /**
     * Revokes all tokens for a user.
     *
     * @param userId the user ID
     */
    void revokeAllUserTokens(String userId);

    /**
     * Deletes a token.
     *
     * @param token the token to delete
     */
    void delete(AuthToken token);

    /**
     * Deletes expired tokens.
     */
    void deleteExpiredTokens();
}
