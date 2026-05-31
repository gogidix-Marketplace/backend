package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.persistence;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for AuthToken entity.
 */
@Repository
public interface TokenRepository extends MongoRepository<AuthToken, String> {

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
     * Finds a valid (not expired, not revoked) token by its value.
     *
     * @param token the token value
     * @return the token if found and valid
     */
    Optional<AuthToken> findByTokenAndRevokedFalseAndExpiresAtAfter(String token, LocalDateTime now);

    /**
     * Deletes all expired tokens.
     */
    void deleteByExpiresAtBefore(LocalDateTime now);
}
