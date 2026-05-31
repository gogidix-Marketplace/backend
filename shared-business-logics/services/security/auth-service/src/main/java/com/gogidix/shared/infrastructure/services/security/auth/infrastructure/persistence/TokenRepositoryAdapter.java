package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.persistence;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import com.gogidix.shared.infrastructure.services.security.auth.domain.port.out.TokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adapter for TokenRepository port.
 * <p>
 * Implements the domain port using Spring Data MongoDB.
 */
@Component
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepositoryPort {

    private final TokenRepository tokenRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public AuthToken save(AuthToken token) {
        return tokenRepository.save(token);
    }

    @Override
    public Optional<AuthToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public Optional<AuthToken> findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public Optional<AuthToken> findValidByToken(String token) {
        return tokenRepository.findByTokenAndRevokedFalseAndExpiresAtAfter(token, LocalDateTime.now());
    }

    @Override
    public void revokeAllUserTokens(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update().set("revoked", true);
        mongoTemplate.updateMulti(query, update, AuthToken.class);
    }

    @Override
    public void delete(AuthToken token) {
        tokenRepository.delete(token);
    }

    @Override
    public void deleteExpiredTokens() {
        tokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}
