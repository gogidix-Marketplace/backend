package com.gogidix.shared.security.application.port.out;

import com.gogidix.shared.security.domain.model.SecurityToken;
import com.gogidix.shared.security.domain.model.TokenType;

/**
 * Output port for publishing security events.
 * Defines the contract for security event publishing implementations.
 */
public interface SecurityEventPublisher {
    
    /**
     * Publishes token created event
     */
    void publishTokenCreated(TokenCreatedEvent event);
    
    /**
     * Publishes token validated event
     */
    void publishTokenValidated(TokenValidatedEvent event);
    
    /**
     * Publishes token revoked event
     */
    void publishTokenRevoked(TokenRevokedEvent event);
    
    /**
     * Publishes token expired event
     */
    void publishTokenExpired(TokenExpiredEvent event);
    
    /**
     * Publishes token refreshed event
     */
    void publishTokenRefreshed(TokenRefreshedEvent event);
    
    /**
     * Publishes authentication failed event
     */
    void publishAuthenticationFailed(AuthenticationFailedEvent event);
    
    /**
     * Publishes authorization denied event
     */
    void publishAuthorizationDenied(AuthorizationDeniedEvent event);
    
    /**
     * Publishes suspicious activity event
     */
    void publishSuspiciousActivity(SuspiciousActivityEvent event);
    
    /**
     * Publishes security policy violation event
     */
    void publishSecurityPolicyViolation(SecurityPolicyViolationEvent event);
    
    /**
     * Token created event
     */
    class TokenCreatedEvent {
        private final String tokenId;
        private final String userId;
        private final TokenType type;
        private final String deviceId;
        private final String ipAddress;
        private final java.time.LocalDateTime timestamp;
        
        public TokenCreatedEvent(String tokenId, String userId, TokenType type,
                               String deviceId, String ipAddress, java.time.LocalDateTime timestamp) {
            this.tokenId = tokenId;
            this.userId = userId;
            this.type = type;
            this.deviceId = deviceId;
            this.ipAddress = ipAddress;
            this.timestamp = timestamp;
        }
        
        public String getTokenId() { return tokenId; }
        public String getUserId() { return userId; }
        public TokenType getType() { return type; }
        public String getDeviceId() { return deviceId; }
        public String getIpAddress() { return ipAddress; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Token validated event
     */
    class TokenValidatedEvent {
        private final String tokenId;
        private final String userId;
        private final boolean successful;
        private final String reason;
        private final String ipAddress;
        private final java.time.LocalDateTime timestamp;
        
        public TokenValidatedEvent(String tokenId, String userId, boolean successful,
                                 String reason, String ipAddress, java.time.LocalDateTime timestamp) {
            this.tokenId = tokenId;
            this.userId = userId;
            this.successful = successful;
            this.reason = reason;
            this.ipAddress = ipAddress;
            this.timestamp = timestamp;
        }
        
        public String getTokenId() { return tokenId; }
        public String getUserId() { return userId; }
        public boolean isSuccessful() { return successful; }
        public String getReason() { return reason; }
        public String getIpAddress() { return ipAddress; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Token revoked event
     */
    class TokenRevokedEvent {
        private final String tokenId;
        private final String userId;
        private final String reason;
        private final String revokedBy;
        private final java.time.LocalDateTime timestamp;
        
        public TokenRevokedEvent(String tokenId, String userId, String reason,
                               String revokedBy, java.time.LocalDateTime timestamp) {
            this.tokenId = tokenId;
            this.userId = userId;
            this.reason = reason;
            this.revokedBy = revokedBy;
            this.timestamp = timestamp;
        }
        
        public String getTokenId() { return tokenId; }
        public String getUserId() { return userId; }
        public String getReason() { return reason; }
        public String getRevokedBy() { return revokedBy; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Token expired event
     */
    class TokenExpiredEvent {
        private final String tokenId;
        private final String userId;
        private final TokenType type;
        private final java.time.LocalDateTime expiredAt;
        private final java.time.LocalDateTime timestamp;
        
        public TokenExpiredEvent(String tokenId, String userId, TokenType type,
                               java.time.LocalDateTime expiredAt, java.time.LocalDateTime timestamp) {
            this.tokenId = tokenId;
            this.userId = userId;
            this.type = type;
            this.expiredAt = expiredAt;
            this.timestamp = timestamp;
        }
        
        public String getTokenId() { return tokenId; }
        public String getUserId() { return userId; }
        public TokenType getType() { return type; }
        public java.time.LocalDateTime getExpiredAt() { return expiredAt; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Token refreshed event
     */
    class TokenRefreshedEvent {
        private final String oldTokenId;
        private final String newTokenId;
        private final String userId;
        private final String ipAddress;
        private final java.time.LocalDateTime timestamp;
        
        public TokenRefreshedEvent(String oldTokenId, String newTokenId, String userId,
                                 String ipAddress, java.time.LocalDateTime timestamp) {
            this.oldTokenId = oldTokenId;
            this.newTokenId = newTokenId;
            this.userId = userId;
            this.ipAddress = ipAddress;
            this.timestamp = timestamp;
        }
        
        public String getOldTokenId() { return oldTokenId; }
        public String getNewTokenId() { return newTokenId; }
        public String getUserId() { return userId; }
        public String getIpAddress() { return ipAddress; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Authentication failed event
     */
    class AuthenticationFailedEvent {
        private final String userId;
        private final String username;
        private final String reason;
        private final String ipAddress;
        private final String userAgent;
        private final int attemptCount;
        private final java.time.LocalDateTime timestamp;
        
        public AuthenticationFailedEvent(String userId, String username, String reason,
                                       String ipAddress, String userAgent, int attemptCount,
                                       java.time.LocalDateTime timestamp) {
            this.userId = userId;
            this.username = username;
            this.reason = reason;
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.attemptCount = attemptCount;
            this.timestamp = timestamp;
        }
        
        public String getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getReason() { return reason; }
        public String getIpAddress() { return ipAddress; }
        public String getUserAgent() { return userAgent; }
        public int getAttemptCount() { return attemptCount; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Authorization denied event
     */
    class AuthorizationDeniedEvent {
        private final String tokenId;
        private final String userId;
        private final String resource;
        private final String operation;
        private final java.util.Set<String> requiredPermissions;
        private final java.util.Set<String> userPermissions;
        private final String ipAddress;
        private final java.time.LocalDateTime timestamp;
        
        public AuthorizationDeniedEvent(String tokenId, String userId, String resource, String operation,
                                      java.util.Set<String> requiredPermissions, java.util.Set<String> userPermissions,
                                      String ipAddress, java.time.LocalDateTime timestamp) {
            this.tokenId = tokenId;
            this.userId = userId;
            this.resource = resource;
            this.operation = operation;
            this.requiredPermissions = requiredPermissions;
            this.userPermissions = userPermissions;
            this.ipAddress = ipAddress;
            this.timestamp = timestamp;
        }
        
        public String getTokenId() { return tokenId; }
        public String getUserId() { return userId; }
        public String getResource() { return resource; }
        public String getOperation() { return operation; }
        public java.util.Set<String> getRequiredPermissions() { return requiredPermissions; }
        public java.util.Set<String> getUserPermissions() { return userPermissions; }
        public String getIpAddress() { return ipAddress; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Suspicious activity event
     */
    class SuspiciousActivityEvent {
        private final String userId;
        private final String activityType;
        private final String description;
        private final String ipAddress;
        private final String userAgent;
        private final String deviceId;
        private final int riskScore;
        private final java.util.Map<String, Object> details;
        private final java.time.LocalDateTime timestamp;
        
        public SuspiciousActivityEvent(String userId, String activityType, String description,
                                     String ipAddress, String userAgent, String deviceId,
                                     int riskScore, java.util.Map<String, Object> details,
                                     java.time.LocalDateTime timestamp) {
            this.userId = userId;
            this.activityType = activityType;
            this.description = description;
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.deviceId = deviceId;
            this.riskScore = riskScore;
            this.details = details;
            this.timestamp = timestamp;
        }
        
        public String getUserId() { return userId; }
        public String getActivityType() { return activityType; }
        public String getDescription() { return description; }
        public String getIpAddress() { return ipAddress; }
        public String getUserAgent() { return userAgent; }
        public String getDeviceId() { return deviceId; }
        public int getRiskScore() { return riskScore; }
        public java.util.Map<String, Object> getDetails() { return details; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
        
        public boolean isHighRisk() { return riskScore >= 80; }
        public boolean isMediumRisk() { return riskScore >= 50 && riskScore < 80; }
        public boolean isLowRisk() { return riskScore < 50; }
    }
    
    /**
     * Security policy violation event
     */
    class SecurityPolicyViolationEvent {
        private final String policyName;
        private final String userId;
        private final String violationType;
        private final String description;
        private final String resource;
        private final String operation;
        private final String ipAddress;
        private final java.util.Map<String, Object> context;
        private final java.time.LocalDateTime timestamp;
        
        public SecurityPolicyViolationEvent(String policyName, String userId, String violationType,
                                          String description, String resource, String operation,
                                          String ipAddress, java.util.Map<String, Object> context,
                                          java.time.LocalDateTime timestamp) {
            this.policyName = policyName;
            this.userId = userId;
            this.violationType = violationType;
            this.description = description;
            this.resource = resource;
            this.operation = operation;
            this.ipAddress = ipAddress;
            this.context = context;
            this.timestamp = timestamp;
        }
        
        public String getPolicyName() { return policyName; }
        public String getUserId() { return userId; }
        public String getViolationType() { return violationType; }
        public String getDescription() { return description; }
        public String getResource() { return resource; }
        public String getOperation() { return operation; }
        public String getIpAddress() { return ipAddress; }
        public java.util.Map<String, Object> getContext() { return context; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
    }
}