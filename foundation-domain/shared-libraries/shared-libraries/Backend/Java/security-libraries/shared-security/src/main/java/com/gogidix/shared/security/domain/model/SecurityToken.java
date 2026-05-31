package com.gogidix.shared.security.domain.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Rich Domain Model for Security Token - HEXAGONAL ARCHITECTURE TEMPLATE
 * 
 * This serves as the GOLD STANDARD template for Agent B JWT/OAuth2 integration.
 * Demonstrates:
 * - Rich business logic for authentication and authorization
 * - Zero infrastructure dependencies (NO Lombok, JPA, etc.)
 * - Immutable design with builder pattern
 * - Comprehensive security validation
 * - JWT and OAuth2 business rules
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B JWT Integration
 * @version 1.0.0
 */
public class SecurityToken {
    
    // Core identifiers (immutable)
    private final String tokenId;
    private final String userId;
    private final String username;
    private final String sessionId;
    private final String correlationId;
    
    // Token metadata (immutable)
    private final TokenType type;
    private final TokenStatus status;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiresAt;
    private final LocalDateTime refreshExpiresAt;
    private final String issuer;
    private final List<String> audience;
    
    // Security context (immutable)
    private final Set<String> roles;
    private final Set<String> permissions;
    private final Set<String> scopes; // OAuth2 scopes
    private final SecurityLevel securityLevel;
    private final boolean multiFactorAuthenticated;
    
    // Device and location context (immutable)
    private final String deviceId;
    private final String deviceFingerprint;
    private final String ipAddress;
    private final String userAgent;
    private final String geolocation;
    
    // OAuth2 specific fields (immutable)
    private final String clientId;
    private final String grantType;
    private final String refreshTokenHash;
    private final String accessTokenHash;
    
    // Business data (immutable)
    private final Map<String, Object> claims;
    private final String parentTokenId;
    private final int tokenVersion;
    
    // Basic constructor for essential fields
    public SecurityToken(String tokenId, String userId, TokenType type, TokenStatus status, 
                        LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.tokenId = Objects.requireNonNull(tokenId, "Token ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.type = Objects.requireNonNull(type, "Token type cannot be null");
        this.status = Objects.requireNonNull(status, "Token status cannot be null");
        this.issuedAt = Objects.requireNonNull(issuedAt, "Issue time cannot be null");
        this.expiresAt = expiresAt;
        
        // Defaults
        this.username = null;
        this.sessionId = null;
        this.correlationId = null;
        this.refreshExpiresAt = null;
        this.issuer = "gogidx-auth-service";
        this.audience = new ArrayList<>();
        this.roles = new HashSet<>();
        this.permissions = new HashSet<>();
        this.scopes = new HashSet<>();
        this.securityLevel = SecurityLevel.STANDARD;
        this.multiFactorAuthenticated = false;
        this.deviceId = null;
        this.deviceFingerprint = null;
        this.ipAddress = null;
        this.userAgent = null;
        this.geolocation = null;
        this.clientId = null;
        this.grantType = null;
        this.refreshTokenHash = null;
        this.accessTokenHash = null;
        this.claims = new HashMap<>();
        this.parentTokenId = null;
        this.tokenVersion = 1;
    }
    
    // Full constructor for complete token creation
    public SecurityToken(String tokenId, String userId, String username, String sessionId, String correlationId,
                        TokenType type, TokenStatus status, LocalDateTime issuedAt, LocalDateTime expiresAt,
                        LocalDateTime refreshExpiresAt, String issuer, List<String> audience,
                        Set<String> roles, Set<String> permissions, Set<String> scopes,
                        SecurityLevel securityLevel, boolean multiFactorAuthenticated,
                        String deviceId, String deviceFingerprint, String ipAddress, String userAgent, String geolocation,
                        String clientId, String grantType, String refreshTokenHash, String accessTokenHash,
                        Map<String, Object> claims, String parentTokenId, int tokenVersion) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.username = username;
        this.sessionId = sessionId;
        this.correlationId = correlationId;
        this.type = type;
        this.status = status;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.refreshExpiresAt = refreshExpiresAt;
        this.issuer = issuer;
        this.audience = audience != null ? new ArrayList<>(audience) : new ArrayList<>();
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
        this.permissions = permissions != null ? new HashSet<>(permissions) : new HashSet<>();
        this.scopes = scopes != null ? new HashSet<>(scopes) : new HashSet<>();
        this.securityLevel = securityLevel != null ? securityLevel : SecurityLevel.STANDARD;
        this.multiFactorAuthenticated = multiFactorAuthenticated;
        this.deviceId = deviceId;
        this.deviceFingerprint = deviceFingerprint;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.geolocation = geolocation;
        this.clientId = clientId;
        this.grantType = grantType;
        this.refreshTokenHash = refreshTokenHash;
        this.accessTokenHash = accessTokenHash;
        this.claims = claims != null ? new HashMap<>(claims) : new HashMap<>();
        this.parentTokenId = parentTokenId;
        this.tokenVersion = tokenVersion;
    }
    
    // ==============================================
    // BUSINESS LOGIC METHODS - JWT/OAuth2 Template
    // ==============================================
    
    /**
     * Business Rule: Check if token is valid for authentication
     */
    public boolean isValid() {
        return tokenId != null && !tokenId.trim().isEmpty() &&
               userId != null && !userId.trim().isEmpty() &&
               status == TokenStatus.ACTIVE &&
               !isExpired() &&
               !isRevoked() &&
               hasRequiredClaims();
    }
    
    /**
     * Business Rule: Check if token has expired
     */
    public boolean isExpired() {
        if (expiresAt == null) return false;
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * Business Rule: Check if token is revoked or suspended
     */
    public boolean isRevoked() {
        return status == TokenStatus.REVOKED || 
               status == TokenStatus.SUSPENDED ||
               status == TokenStatus.EXPIRED;
    }
    
    /**
     * Business Rule: Check if token can be refreshed
     */
    public boolean canBeRefreshed() {
        return type == TokenType.ACCESS_TOKEN &&
               refreshExpiresAt != null &&
               LocalDateTime.now().isBefore(refreshExpiresAt) &&
               status == TokenStatus.ACTIVE &&
               !isCompromised();
    }
    
    /**
     * Business Rule: Check if token shows signs of compromise
     */
    public boolean isCompromised() {
        return status == TokenStatus.COMPROMISED ||
               hasAnomalousUsage() ||
               isFromSuspiciousLocation();
    }
    
    /**
     * Business Rule: Check if token has required JWT claims
     */
    public boolean hasRequiredClaims() {
        return claims != null &&
               claims.containsKey("sub") &&
               claims.containsKey("iss") &&
               claims.containsKey("iat") &&
               (expiresAt != null || claims.containsKey("exp"));
    }
    
    /**
     * Business Rule: Check if token has specific role
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
    
    /**
     * Business Rule: Check if token has any of the required roles
     */
    public boolean hasAnyRole(Set<String> requiredRoles) {
        if (roles == null || requiredRoles == null) return false;
        return roles.stream().anyMatch(requiredRoles::contains);
    }
    
    /**
     * Business Rule: Check if token has all required roles
     */
    public boolean hasAllRoles(Set<String> requiredRoles) {
        if (roles == null || requiredRoles == null) return false;
        return roles.containsAll(requiredRoles);
    }
    
    /**
     * Business Rule: Check if token has specific permission
     */
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }
    
    /**
     * Business Rule: Check if token has OAuth2 scope
     */
    public boolean hasScope(String scope) {
        return scopes != null && scopes.contains(scope);
    }
    
    /**
     * Business Rule: Check if token has any of the required scopes (OAuth2)
     */
    public boolean hasAnyScope(Set<String> requiredScopes) {
        if (scopes == null || requiredScopes == null) return false;
        return scopes.stream().anyMatch(requiredScopes::contains);
    }
    
    /**
     * Business Calculation: Get remaining time until expiration
     */
    public Duration getRemainingTime() {
        if (expiresAt == null) return Duration.ofDays(365); // Never expires
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expiresAt)) return Duration.ZERO;
        
        return Duration.between(now, expiresAt);
    }
    
    /**
     * Business Rule: Check if token will expire soon
     */
    public boolean willExpireSoon(Duration threshold) {
        Duration remaining = getRemainingTime();
        return remaining.compareTo(threshold) <= 0 && !remaining.isZero();
    }
    
    /**
     * Business Rule: Check if token requires MFA for operation
     */
    public boolean requiresMfaForOperation(String operation) {
        if (multiFactorAuthenticated) return false;
        
        // High security operations always require MFA
        if (securityLevel.requiresMultiFactorAuth()) return true;
        
        // Specific sensitive operations
        Set<String> mfaRequiredOperations = Set.of(
            "CHANGE_PASSWORD", "MODIFY_SECURITY_SETTINGS", "DELETE_ACCOUNT",
            "TRANSFER_FUNDS", "ACCESS_SENSITIVE_DATA", "ADMIN_OPERATION",
            "FINANCIAL_TRANSACTION", "DATA_EXPORT"
        );
        
        return mfaRequiredOperations.contains(operation.toUpperCase());
    }
    
    /**
     * Business Rule: Check if token is valid for specific client (OAuth2)
     */
    public boolean isValidForClient(String requestClientId) {
        if (clientId == null) return true; // No client restriction
        return clientId.equals(requestClientId);
    }
    
    /**
     * Business Rule: Check if token is valid for device and location
     */
    public boolean isValidForDevice(String requestDeviceId, String requestIpAddress) {
        // Check device restrictions
        if (deviceId != null && !deviceId.equals(requestDeviceId)) return false;
        
        // Check IP restrictions for high security tokens
        if (securityLevel.enforcesIpRestrictions() && ipAddress != null) {
            return isIpAddressValid(requestIpAddress);
        }
        
        return true;
    }
    
    /**
     * Business Rule: Check if token is for specific audience (JWT)
     */
    public boolean isForAudience(String targetAudience) {
        return audience != null && audience.contains(targetAudience);
    }
    
    /**
     * Business Rule: Check if token supports grant type (OAuth2)
     */
    public boolean supportsGrantType(String requestedGrantType) {
        if (grantType == null) return true; // No grant type restriction
        return grantType.equals(requestedGrantType);
    }
    
    /**
     * Business Calculation: Get token strength score (0-100)
     */
    public int getSecurityScore() {
        int score = 0;
        
        // Base score from security level
        score += securityLevel.getLevel() * 15;
        
        // MFA adds security
        if (multiFactorAuthenticated) score += 20;
        
        // Device restrictions add security
        if (deviceId != null) score += 10;
        if (deviceFingerprint != null) score += 10;
        
        // IP restrictions add security
        if (ipAddress != null) score += 10;
        
        // Short expiration adds security
        Duration remaining = getRemainingTime();
        if (remaining.toHours() <= 1) score += 15;
        else if (remaining.toHours() <= 8) score += 10;
        else if (remaining.toHours() <= 24) score += 5;
        
        // OAuth2 scopes add specificity
        if (scopes != null && !scopes.isEmpty()) score += 5;
        
        return Math.min(100, score);
    }
    
    /**
     * Business Rule: Check if token is high security
     */
    public boolean isHighSecurity() {
        return getSecurityScore() >= 75;
    }
    
    /**
     * Business Rule: Check if token requires audit logging
     */
    public boolean requiresAuditLogging() {
        return isHighSecurity() || 
               multiFactorAuthenticated ||
               securityLevel.getLevel() >= SecurityLevel.HIGH.getLevel();
    }
    
    /**
     * Business Logic: Create revoked version of token
     */
    public SecurityToken revoke(String reason) {
        Map<String, Object> newClaims = new HashMap<>(this.claims);
        newClaims.put("revocation_reason", reason);
        newClaims.put("revoked_at", LocalDateTime.now().toString());
        
        return new SecurityToken(
            this.tokenId, this.userId, this.username, this.sessionId, this.correlationId,
            this.type, TokenStatus.REVOKED, this.issuedAt, this.expiresAt, this.refreshExpiresAt,
            this.issuer, this.audience, this.roles, this.permissions, this.scopes,
            this.securityLevel, this.multiFactorAuthenticated,
            this.deviceId, this.deviceFingerprint, this.ipAddress, this.userAgent, this.geolocation,
            this.clientId, this.grantType, this.refreshTokenHash, this.accessTokenHash,
            newClaims, this.parentTokenId, this.tokenVersion
        );
    }
    
    /**
     * Business Logic: Create refreshed version of token
     */
    public SecurityToken refresh(Duration newExpiration) {
        if (!canBeRefreshed()) {
            throw new IllegalStateException("Token cannot be refreshed: " + getRefreshDenialReason());
        }
        
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> newClaims = new HashMap<>(this.claims);
        newClaims.put("refreshed_at", now.toString());
        newClaims.put("refresh_count", getRefreshCount() + 1);
        
        return new SecurityToken(
            generateNewTokenId(), this.userId, this.username, this.sessionId, this.correlationId,
            this.type, TokenStatus.ACTIVE, now, now.plus(newExpiration), this.refreshExpiresAt,
            this.issuer, this.audience, this.roles, this.permissions, this.scopes,
            this.securityLevel, this.multiFactorAuthenticated,
            this.deviceId, this.deviceFingerprint, this.ipAddress, this.userAgent, this.geolocation,
            this.clientId, this.grantType, this.refreshTokenHash, this.accessTokenHash,
            newClaims, this.tokenId, this.tokenVersion + 1
        );
    }
    
    /**
     * Business Logic: Create enhanced security version
     */
    public SecurityToken enhanceSecurityLevel(SecurityLevel newLevel, String reason) {
        Map<String, Object> newClaims = new HashMap<>(this.claims);
        newClaims.put("security_enhancement_reason", reason);
        newClaims.put("enhanced_at", LocalDateTime.now().toString());
        newClaims.put("previous_security_level", this.securityLevel.name());
        
        return new SecurityToken(
            this.tokenId, this.userId, this.username, this.sessionId, this.correlationId,
            this.type, this.status, this.issuedAt, this.expiresAt, this.refreshExpiresAt,
            this.issuer, this.audience, this.roles, this.permissions, this.scopes,
            newLevel, this.multiFactorAuthenticated,
            this.deviceId, this.deviceFingerprint, this.ipAddress, this.userAgent, this.geolocation,
            this.clientId, this.grantType, this.refreshTokenHash, this.accessTokenHash,
            newClaims, this.parentTokenId, this.tokenVersion
        );
    }
    
    /**
     * Business Logic: Get JWT subject claim
     */
    public String getSubject() {
        return userId; // Subject is typically the user ID
    }
    
    /**
     * Business Logic: Get formatted audience list
     */
    public String getFormattedAudience() {
        if (audience == null || audience.isEmpty()) return "any";
        return String.join(",", audience);
    }
    
    /**
     * Business Logic: Get token usage classification
     */
    public TokenUsageClassification getUsageClassification() {
        if (type == TokenType.API_TOKEN) return TokenUsageClassification.API_ACCESS;
        if (multiFactorAuthenticated && isHighSecurity()) return TokenUsageClassification.HIGH_PRIVILEGE;
        if (securityLevel.getLevel() >= SecurityLevel.HIGH.getLevel()) return TokenUsageClassification.SENSITIVE;
        if (scopes != null && !scopes.isEmpty()) return TokenUsageClassification.SCOPED_ACCESS;
        return TokenUsageClassification.STANDARD;
    }
    
    /**
     * Business Validation: Validate token structure and content
     */
    public boolean isValidTokenStructure() {
        return tokenId != null && !tokenId.trim().isEmpty() &&
               userId != null && !userId.trim().isEmpty() &&
               type != null &&
               status != null &&
               issuedAt != null &&
               securityLevel != null &&
               validateTokenId() &&
               validateClaims() &&
               validateSecurityConstraints();
    }
    
    // Private helper methods for business logic
    private boolean hasAnomalousUsage() {
        Object suspiciousFlag = claims.get("suspicious_activity");
        return suspiciousFlag instanceof Boolean && (Boolean) suspiciousFlag;
    }
    
    private boolean isFromSuspiciousLocation() {
        if (geolocation == null) return false;
        // Business logic for geolocation analysis
        return geolocation.contains("unknown") || geolocation.contains("proxy");
    }
    
    private boolean isIpAddressValid(String requestIp) {
        if (ipAddress == null) return true;
        // Enhanced IP validation could include subnet matching
        return ipAddress.equals(requestIp) || isInSameSubnet(ipAddress, requestIp);
    }
    
    private boolean isInSameSubnet(String originalIp, String requestIp) {
        // Simplified subnet checking - could be enhanced
        if (originalIp == null || requestIp == null) return false;
        String[] originalParts = originalIp.split("\\.");
        String[] requestParts = requestIp.split("\\.");
        
        // Check first 3 octets for same subnet
        return originalParts.length >= 3 && requestParts.length >= 3 &&
               originalParts[0].equals(requestParts[0]) &&
               originalParts[1].equals(requestParts[1]) &&
               originalParts[2].equals(requestParts[2]);
    }
    
    private String getRefreshDenialReason() {
        if (type != TokenType.ACCESS_TOKEN) return "Token type not refreshable";
        if (status != TokenStatus.ACTIVE) return "Token not active";
        if (refreshExpiresAt == null) return "No refresh expiration set";
        if (LocalDateTime.now().isAfter(refreshExpiresAt)) return "Refresh period expired";
        if (isCompromised()) return "Token compromised";
        return "Unknown reason";
    }
    
    private int getRefreshCount() {
        Object count = claims.get("refresh_count");
        return count instanceof Integer ? (Integer) count : 0;
    }
    
    private String generateNewTokenId() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
    
    private boolean validateTokenId() {
        return tokenId.matches("[a-zA-Z0-9\\-]{8,64}");
    }
    
    private boolean validateClaims() {
        if (claims == null) return false;
        
        // Validate required JWT claims
        return claims.containsKey("sub") || userId != null;
    }
    
    private boolean validateSecurityConstraints() {
        // High security tokens must have additional constraints
        if (securityLevel.getLevel() >= SecurityLevel.HIGH.getLevel()) {
            return multiFactorAuthenticated || 
                   (deviceId != null && !deviceId.isEmpty()) ||
                   (ipAddress != null && !ipAddress.isEmpty());
        }
        return true;
    }
    
    // Getters (immutable access)
    public String getTokenId() { return tokenId; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getSessionId() { return sessionId; }
    public String getCorrelationId() { return correlationId; }
    public TokenType getType() { return type; }
    public TokenStatus getStatus() { return status; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getRefreshExpiresAt() { return refreshExpiresAt; }
    public String getIssuer() { return issuer; }
    public List<String> getAudience() { return new ArrayList<>(audience); }
    public Set<String> getRoles() { return new HashSet<>(roles); }
    public Set<String> getPermissions() { return new HashSet<>(permissions); }
    public Set<String> getScopes() { return new HashSet<>(scopes); }
    public SecurityLevel getSecurityLevel() { return securityLevel; }
    public boolean isMultiFactorAuthenticated() { return multiFactorAuthenticated; }
    public String getDeviceId() { return deviceId; }
    public String getDeviceFingerprint() { return deviceFingerprint; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public String getGeolocation() { return geolocation; }
    public String getClientId() { return clientId; }
    public String getGrantType() { return grantType; }
    public String getRefreshTokenHash() { return refreshTokenHash; }
    public String getAccessTokenHash() { return accessTokenHash; }
    public Map<String, Object> getClaims() { return new HashMap<>(claims); }
    public String getParentTokenId() { return parentTokenId; }
    public int getTokenVersion() { return tokenVersion; }
    
    /**
     * Get specific claim with type safety
     */
    public Object getClaim(String claimName) {
        return claims != null ? claims.get(claimName) : null;
    }
    
    public String getStringClaim(String claimName, String defaultValue) {
        Object claim = getClaim(claimName);
        return claim != null ? claim.toString() : defaultValue;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecurityToken)) return false;
        SecurityToken that = (SecurityToken) o;
        return Objects.equals(tokenId, that.tokenId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(tokenId);
    }
    
    @Override
    public String toString() {
        return String.format("SecurityToken{tokenId='%s', userId='%s', type=%s, status=%s, securityLevel=%s}", 
                           tokenId, userId, type, status, securityLevel);
    }
    
    // Static factory methods for common token types
    public static SecurityToken createJwtToken(String userId, String username, Set<String> roles, 
                                             Set<String> permissions, Duration expiration) {
        LocalDateTime now = LocalDateTime.now();
        String tokenId = java.util.UUID.randomUUID().toString().replace("-", "");
        
        Map<String, Object> jwtClaims = new HashMap<>();
        jwtClaims.put("sub", userId);
        jwtClaims.put("iss", "gogidx-auth-service");
        jwtClaims.put("iat", now.toString());
        jwtClaims.put("exp", now.plus(expiration).toString());
        jwtClaims.put("token_type", "JWT");
        
        return new SecurityToken(
            tokenId, userId, username, null, null,
            TokenType.ACCESS_TOKEN, TokenStatus.ACTIVE, now, now.plus(expiration), null,
            "gogidx-auth-service", List.of("gogidx-platform"), roles, permissions, new HashSet<>(),
            SecurityLevel.STANDARD, false,
            null, null, null, null, null,
            null, "authorization_code", null, null,
            jwtClaims, null, 1
        );
    }
    
    public static SecurityToken createOAuth2Token(String userId, String clientId, Set<String> scopes,
                                                Duration expiration, Duration refreshExpiration) {
        LocalDateTime now = LocalDateTime.now();
        String tokenId = java.util.UUID.randomUUID().toString().replace("-", "");
        
        Map<String, Object> oauth2Claims = new HashMap<>();
        oauth2Claims.put("sub", userId);
        oauth2Claims.put("client_id", clientId);
        oauth2Claims.put("scope", String.join(" ", scopes));
        oauth2Claims.put("token_type", "Bearer");
        oauth2Claims.put("iat", now.toString());
        
        return new SecurityToken(
            tokenId, userId, null, null, null,
            TokenType.ACCESS_TOKEN, TokenStatus.ACTIVE, now, now.plus(expiration), now.plus(refreshExpiration),
            "gogidx-oauth-service", List.of(clientId), new HashSet<>(), new HashSet<>(), scopes,
            SecurityLevel.STANDARD, false,
            null, null, null, null, null,
            clientId, "client_credentials", null, null,
            oauth2Claims, null, 1
        );
    }
    
    public static SecurityToken createApiToken(String userId, String apiKeyName, Set<String> permissions,
                                             Duration expiration, SecurityLevel securityLevel) {
        LocalDateTime now = LocalDateTime.now();
        String tokenId = java.util.UUID.randomUUID().toString().replace("-", "");
        
        Map<String, Object> apiClaims = new HashMap<>();
        apiClaims.put("sub", userId);
        apiClaims.put("api_key_name", apiKeyName);
        apiClaims.put("token_type", "API");
        apiClaims.put("iat", now.toString());
        
        return new SecurityToken(
            tokenId, userId, apiKeyName, null, null,
            TokenType.API_TOKEN, TokenStatus.ACTIVE, now, expiration != null ? now.plus(expiration) : null, null,
            "gogidx-api-service", List.of("gogidx-api"), new HashSet<>(), permissions, new HashSet<>(),
            securityLevel, true, // API tokens are considered pre-authenticated
            null, null, null, null, null,
            null, "api_key", null, null,
            apiClaims, null, 1
        );
    }
    
    // Builder pattern for complex token creation
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String tokenId;
        private String userId;
        private String username;
        private String sessionId;
        private String correlationId;
        private TokenType type = TokenType.ACCESS_TOKEN;
        private TokenStatus status = TokenStatus.ACTIVE;
        private LocalDateTime issuedAt = LocalDateTime.now();
        private LocalDateTime expiresAt;
        private LocalDateTime refreshExpiresAt;
        private String issuer = "gogidx-auth-service";
        private List<String> audience = new ArrayList<>();
        private Set<String> roles = new HashSet<>();
        private Set<String> permissions = new HashSet<>();
        private Set<String> scopes = new HashSet<>();
        private SecurityLevel securityLevel = SecurityLevel.STANDARD;
        private boolean multiFactorAuthenticated = false;
        private String deviceId;
        private String deviceFingerprint;
        private String ipAddress;
        private String userAgent;
        private String geolocation;
        private String clientId;
        private String grantType;
        private String refreshTokenHash;
        private String accessTokenHash;
        private Map<String, Object> claims = new HashMap<>();
        private String parentTokenId;
        private int tokenVersion = 1;
        
        public Builder tokenId(String tokenId) { this.tokenId = tokenId; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public Builder type(TokenType type) { this.type = type; return this; }
        public Builder status(TokenStatus status) { this.status = status; return this; }
        public Builder issuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; return this; }
        public Builder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public Builder refreshExpiresAt(LocalDateTime refreshExpiresAt) { this.refreshExpiresAt = refreshExpiresAt; return this; }
        public Builder issuer(String issuer) { this.issuer = issuer; return this; }
        public Builder audience(List<String> audience) { this.audience = audience; return this; }
        public Builder roles(Set<String> roles) { this.roles = roles; return this; }
        public Builder permissions(Set<String> permissions) { this.permissions = permissions; return this; }
        public Builder scopes(Set<String> scopes) { this.scopes = scopes; return this; }
        public Builder securityLevel(SecurityLevel securityLevel) { this.securityLevel = securityLevel; return this; }
        public Builder multiFactorAuthenticated(boolean multiFactorAuthenticated) { this.multiFactorAuthenticated = multiFactorAuthenticated; return this; }
        public Builder deviceId(String deviceId) { this.deviceId = deviceId; return this; }
        public Builder deviceFingerprint(String deviceFingerprint) { this.deviceFingerprint = deviceFingerprint; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public Builder geolocation(String geolocation) { this.geolocation = geolocation; return this; }
        public Builder clientId(String clientId) { this.clientId = clientId; return this; }
        public Builder grantType(String grantType) { this.grantType = grantType; return this; }
        public Builder refreshTokenHash(String refreshTokenHash) { this.refreshTokenHash = refreshTokenHash; return this; }
        public Builder accessTokenHash(String accessTokenHash) { this.accessTokenHash = accessTokenHash; return this; }
        public Builder claims(Map<String, Object> claims) { this.claims = claims; return this; }
        public Builder parentTokenId(String parentTokenId) { this.parentTokenId = parentTokenId; return this; }
        public Builder tokenVersion(int tokenVersion) { this.tokenVersion = tokenVersion; return this; }
        
        public Builder addRole(String role) { this.roles.add(role); return this; }
        public Builder addPermission(String permission) { this.permissions.add(permission); return this; }
        public Builder addScope(String scope) { this.scopes.add(scope); return this; }
        public Builder addClaim(String key, Object value) { this.claims.put(key, value); return this; }
        public Builder addAudience(String audience) { this.audience.add(audience); return this; }
        
        public SecurityToken build() {
            if (tokenId == null) {
                tokenId = java.util.UUID.randomUUID().toString().replace("-", "");
            }
            
            return new SecurityToken(tokenId, userId, username, sessionId, correlationId,
                                   type, status, issuedAt, expiresAt, refreshExpiresAt,
                                   issuer, audience, roles, permissions, scopes,
                                   securityLevel, multiFactorAuthenticated,
                                   deviceId, deviceFingerprint, ipAddress, userAgent, geolocation,
                                   clientId, grantType, refreshTokenHash, accessTokenHash,
                                   claims, parentTokenId, tokenVersion);
        }
    }
}

enum TokenUsageClassification {
    STANDARD, SENSITIVE, HIGH_PRIVILEGE, API_ACCESS, SCOPED_ACCESS
}