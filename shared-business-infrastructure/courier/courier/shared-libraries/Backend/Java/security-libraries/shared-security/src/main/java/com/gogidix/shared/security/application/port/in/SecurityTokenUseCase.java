package com.gogidix.shared.security.application.port.in;

import com.gogidix.shared.security.domain.model.SecurityToken;
import com.gogidix.shared.security.domain.model.TokenType;
import com.gogidix.shared.security.domain.model.SecurityLevel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Input port for security token management use cases.
 * Defines the contract for token operations from the application layer.
 */
public interface SecurityTokenUseCase {
    
    /**
     * Creates a new security token
     */
    TokenResult createToken(CreateTokenRequest request);
    
    /**
     * Validates a security token
     */
    ValidationResult validateToken(String tokenId, String rawToken);
    
    /**
     * Refreshes an access token using a refresh token
     */
    TokenResult refreshToken(String refreshTokenId, String refreshToken);
    
    /**
     * Revokes a security token
     */
    TokenResult revokeToken(String tokenId, String reason);
    
    /**
     * Suspends a token temporarily
     */
    TokenResult suspendToken(String tokenId, String reason, long suspendDurationSeconds);
    
    /**
     * Reactivates a suspended token
     */
    TokenResult reactivateToken(String tokenId);
    
    /**
     * Gets token information
     */
    Optional<SecurityToken> getToken(String tokenId);
    
    /**
     * Lists tokens for a user
     */
    List<SecurityToken> getUserTokens(String userId, TokenType type);
    
    /**
     * Lists active sessions for a user
     */
    List<SecurityToken> getActiveSessions(String userId);
    
    /**
     * Checks if user has required permissions
     */
    AuthorizationResult authorize(String tokenId, Set<String> requiredPermissions);
    
    /**
     * Checks if user has required roles
     */
    AuthorizationResult authorizeRoles(String tokenId, Set<String> requiredRoles);
    
    /**
     * Gets user permissions from token
     */
    Set<String> getUserPermissions(String tokenId);
    
    /**
     * Gets user roles from token
     */
    Set<String> getUserRoles(String tokenId);
    
    /**
     * Revokes all tokens for a user
     */
    BulkTokenResult revokeAllUserTokens(String userId, String reason);
    
    /**
     * Revokes all tokens of a specific type for a user
     */
    BulkTokenResult revokeUserTokensByType(String userId, TokenType type, String reason);
    
    /**
     * Cleans up expired tokens
     */
    CleanupResult cleanupExpiredTokens();
    
    /**
     * Gets token usage statistics
     */
    TokenStatistics getTokenStatistics(String userId);
    
    /**
     * Validates token against security policies
     */
    PolicyValidationResult validateSecurityPolicy(String tokenId, String operation);
    
    /**
     * Request to create a new token
     */
    class CreateTokenRequest {
        private final String userId;
        private final String username;
        private final TokenType type;
        private final Set<String> roles;
        private final Set<String> permissions;
        private final SecurityLevel securityLevel;
        private final String deviceId;
        private final String ipAddress;
        private final String userAgent;
        private final Long customExpirationSeconds;
        private final boolean multiFactorAuthenticated;
        
        public CreateTokenRequest(String userId, String username, TokenType type,
                                Set<String> roles, Set<String> permissions,
                                SecurityLevel securityLevel, String deviceId,
                                String ipAddress, String userAgent,
                                Long customExpirationSeconds, boolean multiFactorAuthenticated) {
            this.userId = userId;
            this.username = username;
            this.type = type;
            this.roles = roles;
            this.permissions = permissions;
            this.securityLevel = securityLevel;
            this.deviceId = deviceId;
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.customExpirationSeconds = customExpirationSeconds;
            this.multiFactorAuthenticated = multiFactorAuthenticated;
        }
        
        // Getters
        public String getUserId() { return userId; }
        public String getUsername() { return username; }
        public TokenType getType() { return type; }
        public Set<String> getRoles() { return roles; }
        public Set<String> getPermissions() { return permissions; }
        public SecurityLevel getSecurityLevel() { return securityLevel; }
        public String getDeviceId() { return deviceId; }
        public String getIpAddress() { return ipAddress; }
        public String getUserAgent() { return userAgent; }
        public Long getCustomExpirationSeconds() { return customExpirationSeconds; }
        public boolean isMultiFactorAuthenticated() { return multiFactorAuthenticated; }
    }
    
    /**
     * Token operation result
     */
    class TokenResult {
        private final boolean success;
        private final SecurityToken token;
        private final String message;
        private final Exception error;
        
        public TokenResult(boolean success, SecurityToken token, String message, Exception error) {
            this.success = success;
            this.token = token;
            this.message = message;
            this.error = error;
        }
        
        public static TokenResult success(SecurityToken token, String message) {
            return new TokenResult(true, token, message, null);
        }
        
        public static TokenResult failure(String message, Exception error) {
            return new TokenResult(false, null, message, error);
        }
        
        public boolean isSuccess() { return success; }
        public SecurityToken getToken() { return token; }
        public String getMessage() { return message; }
        public Optional<Exception> getError() { return Optional.ofNullable(error); }
    }
    
    /**
     * Token validation result
     */
    class ValidationResult {
        private final boolean valid;
        private final SecurityToken token;
        private final String reason;
        private final long remainingTimeSeconds;
        
        public ValidationResult(boolean valid, SecurityToken token, String reason, long remainingTimeSeconds) {
            this.valid = valid;
            this.token = token;
            this.reason = reason;
            this.remainingTimeSeconds = remainingTimeSeconds;
        }
        
        public static ValidationResult valid(SecurityToken token, long remainingTimeSeconds) {
            return new ValidationResult(true, token, "Token is valid", remainingTimeSeconds);
        }
        
        public static ValidationResult invalid(String reason) {
            return new ValidationResult(false, null, reason, 0);
        }
        
        public boolean isValid() { return valid; }
        public SecurityToken getToken() { return token; }
        public String getReason() { return reason; }
        public long getRemainingTimeSeconds() { return remainingTimeSeconds; }
        public boolean willExpireSoon() { return remainingTimeSeconds <= 300; } // 5 minutes
    }
    
    /**
     * Authorization result
     */
    class AuthorizationResult {
        private final boolean authorized;
        private final Set<String> grantedPermissions;
        private final Set<String> missingPermissions;
        private final String reason;
        
        public AuthorizationResult(boolean authorized, Set<String> grantedPermissions,
                                 Set<String> missingPermissions, String reason) {
            this.authorized = authorized;
            this.grantedPermissions = grantedPermissions;
            this.missingPermissions = missingPermissions;
            this.reason = reason;
        }
        
        public static AuthorizationResult authorized(Set<String> grantedPermissions) {
            return new AuthorizationResult(true, grantedPermissions, Set.of(), "Access granted");
        }
        
        public static AuthorizationResult denied(Set<String> missingPermissions, String reason) {
            return new AuthorizationResult(false, Set.of(), missingPermissions, reason);
        }
        
        public boolean isAuthorized() { return authorized; }
        public Set<String> getGrantedPermissions() { return grantedPermissions; }
        public Set<String> getMissingPermissions() { return missingPermissions; }
        public String getReason() { return reason; }
    }
    
    /**
     * Bulk token operation result
     */
    class BulkTokenResult {
        private final int totalTokens;
        private final int successfulOperations;
        private final int failedOperations;
        private final List<String> failedTokenIds;
        private final String message;
        
        public BulkTokenResult(int totalTokens, int successfulOperations, int failedOperations,
                             List<String> failedTokenIds, String message) {
            this.totalTokens = totalTokens;
            this.successfulOperations = successfulOperations;
            this.failedOperations = failedOperations;
            this.failedTokenIds = failedTokenIds;
            this.message = message;
        }
        
        public int getTotalTokens() { return totalTokens; }
        public int getSuccessfulOperations() { return successfulOperations; }
        public int getFailedOperations() { return failedOperations; }
        public List<String> getFailedTokenIds() { return failedTokenIds; }
        public String getMessage() { return message; }
        
        public boolean isCompletelySuccessful() { return failedOperations == 0; }
        public double getSuccessRate() {
            return totalTokens > 0 ? (double) successfulOperations / totalTokens : 0.0;
        }
    }
    
    /**
     * Token cleanup result
     */
    class CleanupResult {
        private final int expiredTokensFound;
        private final int tokensRemoved;
        private final long cleanupTimeMs;
        private final String summary;
        
        public CleanupResult(int expiredTokensFound, int tokensRemoved, long cleanupTimeMs, String summary) {
            this.expiredTokensFound = expiredTokensFound;
            this.tokensRemoved = tokensRemoved;
            this.cleanupTimeMs = cleanupTimeMs;
            this.summary = summary;
        }
        
        public int getExpiredTokensFound() { return expiredTokensFound; }
        public int getTokensRemoved() { return tokensRemoved; }
        public long getCleanupTimeMs() { return cleanupTimeMs; }
        public String getSummary() { return summary; }
    }
    
    /**
     * Token usage statistics
     */
    class TokenStatistics {
        private final String userId;
        private final int totalTokens;
        private final int activeTokens;
        private final int expiredTokens;
        private final int revokedTokens;
        private final java.util.Map<TokenType, Integer> tokensByType;
        private final java.time.LocalDateTime lastActivity;
        
        public TokenStatistics(String userId, int totalTokens, int activeTokens, int expiredTokens,
                             int revokedTokens, java.util.Map<TokenType, Integer> tokensByType,
                             java.time.LocalDateTime lastActivity) {
            this.userId = userId;
            this.totalTokens = totalTokens;
            this.activeTokens = activeTokens;
            this.expiredTokens = expiredTokens;
            this.revokedTokens = revokedTokens;
            this.tokensByType = tokensByType;
            this.lastActivity = lastActivity;
        }
        
        public String getUserId() { return userId; }
        public int getTotalTokens() { return totalTokens; }
        public int getActiveTokens() { return activeTokens; }
        public int getExpiredTokens() { return expiredTokens; }
        public int getRevokedTokens() { return revokedTokens; }
        public java.util.Map<TokenType, Integer> getTokensByType() { return tokensByType; }
        public java.time.LocalDateTime getLastActivity() { return lastActivity; }
    }
    
    /**
     * Security policy validation result
     */
    class PolicyValidationResult {
        private final boolean allowed;
        private final String policyName;
        private final String reason;
        private final Set<String> requiredActions;
        
        public PolicyValidationResult(boolean allowed, String policyName, String reason, Set<String> requiredActions) {
            this.allowed = allowed;
            this.policyName = policyName;
            this.reason = reason;
            this.requiredActions = requiredActions;
        }
        
        public static PolicyValidationResult allowed(String policyName) {
            return new PolicyValidationResult(true, policyName, "Policy validation passed", Set.of());
        }
        
        public static PolicyValidationResult denied(String policyName, String reason, Set<String> requiredActions) {
            return new PolicyValidationResult(false, policyName, reason, requiredActions);
        }
        
        public boolean isAllowed() { return allowed; }
        public String getPolicyName() { return policyName; }
        public String getReason() { return reason; }
        public Set<String> getRequiredActions() { return requiredActions; }
    }
}