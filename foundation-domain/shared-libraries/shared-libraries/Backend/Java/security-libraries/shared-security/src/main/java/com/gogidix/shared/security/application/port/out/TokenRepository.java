package com.gogidix.shared.security.application.port.out;

import com.gogidix.shared.security.domain.model.SecurityToken;
import com.gogidix.shared.security.domain.model.TokenType;
import com.gogidix.shared.security.domain.model.TokenStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Output port for token persistence operations.
 * Defines the contract for token repository implementations.
 */
public interface TokenRepository {
    
    /**
     * Saves a security token
     */
    SecurityToken save(SecurityToken token);
    
    /**
     * Finds a token by its ID
     */
    Optional<SecurityToken> findById(String tokenId);
    
    /**
     * Finds a token by user ID and token ID
     */
    Optional<SecurityToken> findByUserIdAndTokenId(String userId, String tokenId);
    
    /**
     * Finds tokens by user ID
     */
    List<SecurityToken> findByUserId(String userId);
    
    /**
     * Finds tokens by user ID and type
     */
    List<SecurityToken> findByUserIdAndType(String userId, TokenType type);
    
    /**
     * Finds tokens by user ID and status
     */
    List<SecurityToken> findByUserIdAndStatus(String userId, TokenStatus status);
    
    /**
     * Finds active tokens by user ID
     */
    List<SecurityToken> findActiveTokensByUserId(String userId);
    
    /**
     * Finds tokens by status
     */
    List<SecurityToken> findByStatus(TokenStatus status);
    
    /**
     * Finds tokens by type and status
     */
    List<SecurityToken> findByTypeAndStatus(TokenType type, TokenStatus status);
    
    /**
     * Finds expired tokens
     */
    List<SecurityToken> findExpiredTokens();
    
    /**
     * Finds tokens expiring within the specified duration
     */
    List<SecurityToken> findTokensExpiringWithin(long seconds);
    
    /**
     * Finds tokens by device ID
     */
    List<SecurityToken> findByDeviceId(String deviceId);
    
    /**
     * Finds tokens by IP address
     */
    List<SecurityToken> findByIpAddress(String ipAddress);
    
    /**
     * Finds tokens by session ID
     */
    Optional<SecurityToken> findBySessionId(String sessionId);
    
    /**
     * Updates token status
     */
    SecurityToken updateStatus(String tokenId, TokenStatus status);
    
    /**
     * Deletes a token by ID
     */
    void deleteById(String tokenId);
    
    /**
     * Deletes all tokens for a user
     */
    int deleteByUserId(String userId);
    
    /**
     * Deletes tokens by user ID and type
     */
    int deleteByUserIdAndType(String userId, TokenType type);
    
    /**
     * Deletes expired tokens
     */
    int deleteExpiredTokens();
    
    /**
     * Deletes tokens by status
     */
    int deleteByStatus(TokenStatus status);
    
    /**
     * Counts tokens by user ID
     */
    long countByUserId(String userId);
    
    /**
     * Counts active tokens by user ID
     */
    long countActiveTokensByUserId(String userId);
    
    /**
     * Counts tokens by user ID and type
     */
    long countByUserIdAndType(String userId, TokenType type);
    
    /**
     * Counts tokens by status
     */
    long countByStatus(TokenStatus status);
    
    /**
     * Counts expired tokens
     */
    long countExpiredTokens();
    
    /**
     * Checks if token exists by ID
     */
    boolean existsById(String tokenId);
    
    /**
     * Checks if user has active tokens
     */
    boolean hasActiveTokens(String userId);
    
    /**
     * Gets token statistics for a user
     */
    TokenStatistics getStatisticsForUser(String userId);
    
    /**
     * Gets global token statistics
     */
    TokenStatistics getGlobalStatistics();
    
    /**
     * Finds tokens with custom filter criteria
     */
    List<SecurityToken> findWithFilter(TokenFilter filter);
    
    /**
     * Gets the last activity timestamp for a user
     */
    Optional<LocalDateTime> getLastActivityTime(String userId);
    
    /**
     * Updates the last activity timestamp for a token
     */
    void updateLastActivity(String tokenId, LocalDateTime timestamp);
    
    /**
     * Token statistics data class
     */
    class TokenStatistics {
        private final long totalTokens;
        private final long activeTokens;
        private final long expiredTokens;
        private final long revokedTokens;
        private final long suspendedTokens;
        private final java.util.Map<TokenType, Long> tokensByType;
        private final LocalDateTime oldestToken;
        private final LocalDateTime newestToken;
        
        public TokenStatistics(long totalTokens, long activeTokens, long expiredTokens,
                             long revokedTokens, long suspendedTokens,
                             java.util.Map<TokenType, Long> tokensByType,
                             LocalDateTime oldestToken, LocalDateTime newestToken) {
            this.totalTokens = totalTokens;
            this.activeTokens = activeTokens;
            this.expiredTokens = expiredTokens;
            this.revokedTokens = revokedTokens;
            this.suspendedTokens = suspendedTokens;
            this.tokensByType = tokensByType;
            this.oldestToken = oldestToken;
            this.newestToken = newestToken;
        }
        
        public long getTotalTokens() { return totalTokens; }
        public long getActiveTokens() { return activeTokens; }
        public long getExpiredTokens() { return expiredTokens; }
        public long getRevokedTokens() { return revokedTokens; }
        public long getSuspendedTokens() { return suspendedTokens; }
        public java.util.Map<TokenType, Long> getTokensByType() { return tokensByType; }
        public LocalDateTime getOldestToken() { return oldestToken; }
        public LocalDateTime getNewestToken() { return newestToken; }
        
        public double getActiveTokenRatio() {
            return totalTokens > 0 ? (double) activeTokens / totalTokens : 0.0;
        }
    }
    
    /**
     * Token filter for complex queries
     */
    class TokenFilter {
        private final String userId;
        private final TokenType type;
        private final TokenStatus status;
        private final LocalDateTime createdAfter;
        private final LocalDateTime createdBefore;
        private final LocalDateTime expiresAfter;
        private final LocalDateTime expiresBefore;
        private final String deviceId;
        private final String ipAddress;
        private final Boolean multiFactorAuthenticated;
        private final String sessionId;
        private final int limit;
        
        public TokenFilter(String userId, TokenType type, TokenStatus status,
                         LocalDateTime createdAfter, LocalDateTime createdBefore,
                         LocalDateTime expiresAfter, LocalDateTime expiresBefore,
                         String deviceId, String ipAddress, Boolean multiFactorAuthenticated,
                         String sessionId, int limit) {
            this.userId = userId;
            this.type = type;
            this.status = status;
            this.createdAfter = createdAfter;
            this.createdBefore = createdBefore;
            this.expiresAfter = expiresAfter;
            this.expiresBefore = expiresBefore;
            this.deviceId = deviceId;
            this.ipAddress = ipAddress;
            this.multiFactorAuthenticated = multiFactorAuthenticated;
            this.sessionId = sessionId;
            this.limit = limit;
        }
        
        public static TokenFilter forUser(String userId) {
            return new TokenFilter(userId, null, null, null, null, null, null,
                                 null, null, null, null, 100);
        }
        
        public static TokenFilter forUserAndType(String userId, TokenType type) {
            return new TokenFilter(userId, type, null, null, null, null, null,
                                 null, null, null, null, 100);
        }
        
        public static TokenFilter activeTokens() {
            return new TokenFilter(null, null, TokenStatus.ACTIVE, null, null, null, null,
                                 null, null, null, null, 1000);
        }
        
        public static TokenFilter expiredTokens() {
            return new TokenFilter(null, null, null, null, null, null,
                                 LocalDateTime.now(), null, null, null, null, 1000);
        }
        
        // Getters
        public Optional<String> getUserId() { return Optional.ofNullable(userId); }
        public Optional<TokenType> getType() { return Optional.ofNullable(type); }
        public Optional<TokenStatus> getStatus() { return Optional.ofNullable(status); }
        public Optional<LocalDateTime> getCreatedAfter() { return Optional.ofNullable(createdAfter); }
        public Optional<LocalDateTime> getCreatedBefore() { return Optional.ofNullable(createdBefore); }
        public Optional<LocalDateTime> getExpiresAfter() { return Optional.ofNullable(expiresAfter); }
        public Optional<LocalDateTime> getExpiresBefore() { return Optional.ofNullable(expiresBefore); }
        public Optional<String> getDeviceId() { return Optional.ofNullable(deviceId); }
        public Optional<String> getIpAddress() { return Optional.ofNullable(ipAddress); }
        public Optional<Boolean> getMultiFactorAuthenticated() { return Optional.ofNullable(multiFactorAuthenticated); }
        public Optional<String> getSessionId() { return Optional.ofNullable(sessionId); }
        public int getLimit() { return limit; }
    }
}