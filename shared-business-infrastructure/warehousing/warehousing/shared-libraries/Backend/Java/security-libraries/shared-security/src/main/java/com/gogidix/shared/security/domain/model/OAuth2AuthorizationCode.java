package com.gogidix.shared.security.domain.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Set;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Rich Domain Model for OAuth2 Authorization Code - HEXAGONAL ARCHITECTURE TEMPLATE
 * 
 * This serves as the OAuth2 authorization code domain model for Agent B services.
 * Demonstrates:
 * - OAuth2 authorization code flow business logic
 * - Zero infrastructure dependencies (NO Lombok, JPA, etc.)
 * - Immutable design with builder pattern
 * - OAuth2 security validation and state management
 * - PKCE support and security enhancements
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B OAuth2 Integration
 * @version 1.0.0
 */
public class OAuth2AuthorizationCode {
    
    // Core OAuth2 fields (immutable)
    private final String code;
    private final String clientId;
    private final String userId;
    private final String redirectUri;
    private final Set<String> scopes;
    private final String state;
    
    // Security and timing (immutable)
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiresAt;
    private final SecurityLevel securityLevel;
    private final boolean isUsed;
    private final LocalDateTime usedAt;
    
    // PKCE (Proof Key for Code Exchange) support (immutable)
    private final String codeChallenge;
    private final String codeChallengeMethod;
    
    // Additional context (immutable)
    private final String sessionId;
    private final String ipAddress;
    private final String userAgent;
    private final String deviceFingerprint;
    
    // Business metadata (immutable)
    private final Map<String, Object> additionalParameters;
    private final String authenticationMethod;
    private final boolean multiFactorAuthenticated;
    private final int usageCount;
    
    // Basic constructor for essential fields
    public OAuth2AuthorizationCode(String code, String clientId, String userId, 
                                  String redirectUri, Set<String> scopes, Duration expiration) {
        this.code = Objects.requireNonNull(code, "Authorization code cannot be null");
        this.clientId = Objects.requireNonNull(clientId, "Client ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.redirectUri = Objects.requireNonNull(redirectUri, "Redirect URI cannot be null");
        this.scopes = scopes != null ? new HashSet<>(scopes) : new HashSet<>();
        
        LocalDateTime now = LocalDateTime.now();
        this.issuedAt = now;
        this.expiresAt = expiration != null ? now.plus(expiration) : now.plusMinutes(10); // Default 10 min
        
        // Defaults
        this.state = null;
        this.securityLevel = SecurityLevel.STANDARD;
        this.isUsed = false;
        this.usedAt = null;
        this.codeChallenge = null;
        this.codeChallengeMethod = null;
        this.sessionId = null;
        this.ipAddress = null;
        this.userAgent = null;
        this.deviceFingerprint = null;
        this.additionalParameters = new HashMap<>();
        this.authenticationMethod = "authorization_code";
        this.multiFactorAuthenticated = false;
        this.usageCount = 0;
    }
    
    // Full constructor for complete authorization code creation
    public OAuth2AuthorizationCode(String code, String clientId, String userId, String redirectUri,
                                  Set<String> scopes, String state, LocalDateTime issuedAt, LocalDateTime expiresAt,
                                  SecurityLevel securityLevel, boolean isUsed, LocalDateTime usedAt,
                                  String codeChallenge, String codeChallengeMethod,
                                  String sessionId, String ipAddress, String userAgent, String deviceFingerprint,
                                  Map<String, Object> additionalParameters, String authenticationMethod,
                                  boolean multiFactorAuthenticated, int usageCount) {
        this.code = code;
        this.clientId = clientId;
        this.userId = userId;
        this.redirectUri = redirectUri;
        this.scopes = scopes != null ? new HashSet<>(scopes) : new HashSet<>();
        this.state = state;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.securityLevel = securityLevel != null ? securityLevel : SecurityLevel.STANDARD;
        this.isUsed = isUsed;
        this.usedAt = usedAt;
        this.codeChallenge = codeChallenge;
        this.codeChallengeMethod = codeChallengeMethod;
        this.sessionId = sessionId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.deviceFingerprint = deviceFingerprint;
        this.additionalParameters = additionalParameters != null ? new HashMap<>(additionalParameters) : new HashMap<>();
        this.authenticationMethod = authenticationMethod;
        this.multiFactorAuthenticated = multiFactorAuthenticated;
        this.usageCount = usageCount;
    }
    
    // ==============================================
    // OAUTH2 BUSINESS LOGIC METHODS - Template
    // ==============================================
    
    /**
     * Business Rule: Check if authorization code is valid and can be used
     */
    public boolean isValid() {
        return code != null && !code.trim().isEmpty() &&
               clientId != null && !clientId.trim().isEmpty() &&
               userId != null && !userId.trim().isEmpty() &&
               redirectUri != null && !redirectUri.trim().isEmpty() &&
               !isExpired() &&
               !isUsed &&
               usageCount == 0 &&
               hasValidStructure();
    }
    
    /**
     * Business Rule: Check if authorization code has expired
     */
    public boolean isExpired() {
        if (expiresAt == null) return false;
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * Business Rule: Check if authorization code has been used
     */
    public boolean isAlreadyUsed() {
        return isUsed || usageCount > 0 || usedAt != null;
    }
    
    /**
     * Business Rule: Check if authorization code has valid structure
     */
    public boolean hasValidStructure() {
        return code != null && code.length() >= 16 &&
               clientId != null && !clientId.trim().isEmpty() &&
               redirectUri != null && isValidRedirectUri() &&
               validateScopes() &&
               validatePkceIfPresent();
    }
    
    /**
     * Business Rule: Check if redirect URI is valid
     */
    public boolean isValidRedirectUri() {
        if (redirectUri == null) return false;
        
        // Must be HTTPS for production or localhost for development
        return redirectUri.startsWith("https://") || 
               redirectUri.startsWith("http://localhost") ||
               redirectUri.startsWith("http://127.0.0.1");
    }
    
    /**
     * Business Rule: Check if scopes are valid
     */
    public boolean hasScope(String scope) {
        return scopes != null && scopes.contains(scope);
    }
    
    /**
     * Business Rule: Check if has any of the required scopes
     */
    public boolean hasAnyScope(Set<String> requiredScopes) {
        if (scopes == null || requiredScopes == null) return false;
        return scopes.stream().anyMatch(requiredScopes::contains);
    }
    
    /**
     * Business Rule: Check if PKCE is enabled
     */
    public boolean isPkceEnabled() {
        return codeChallenge != null && !codeChallenge.trim().isEmpty() &&
               codeChallengeMethod != null && !codeChallengeMethod.trim().isEmpty();
    }
    
    /**
     * Business Rule: Validate PKCE code verifier
     */
    public boolean validatePkceCodeVerifier(String codeVerifier) {
        if (!isPkceEnabled()) return true; // PKCE not required
        if (codeVerifier == null || codeVerifier.trim().isEmpty()) return false;
        
        // Validate code verifier format (43-128 characters, URL-safe)
        if (codeVerifier.length() < 43 || codeVerifier.length() > 128) return false;
        if (!codeVerifier.matches("[A-Za-z0-9._~-]+")) return false;
        
        // Validate against challenge based on method
        return switch (codeChallengeMethod.toLowerCase()) {
            case "plain" -> codeChallenge.equals(codeVerifier);
            case "s256" -> validateSha256Challenge(codeVerifier);
            default -> false;
        };
    }
    
    /**
     * Business Calculation: Get remaining time until expiration
     */
    public Duration getRemainingTime() {
        if (expiresAt == null) return Duration.ZERO;
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expiresAt)) return Duration.ZERO;
        
        return Duration.between(now, expiresAt);
    }
    
    /**
     * Business Rule: Check if code will expire soon
     */
    public boolean willExpireSoon(Duration threshold) {
        Duration remaining = getRemainingTime();
        return remaining.compareTo(threshold) <= 0 && !remaining.isZero();
    }
    
    /**
     * Business Calculation: Get age of authorization code
     */
    public Duration getAge() {
        if (issuedAt == null) return Duration.ZERO;
        return Duration.between(issuedAt, LocalDateTime.now());
    }
    
    /**
     * Business Rule: Check if code is fresh (recently issued)
     */
    public boolean isFresh(Duration freshnessThreshold) {
        return getAge().compareTo(freshnessThreshold) <= 0;
    }
    
    /**
     * Business Rule: Check if code is for specific client
     */
    public boolean isForClient(String requestClientId) {
        return clientId != null && clientId.equals(requestClientId);
    }
    
    /**
     * Business Rule: Check if redirect URI matches
     */
    public boolean isForRedirectUri(String requestRedirectUri) {
        return redirectUri != null && redirectUri.equals(requestRedirectUri);
    }
    
    /**
     * Business Rule: Check if state parameter matches (CSRF protection)
     */
    public boolean isForState(String requestState) {
        if (state == null && requestState == null) return true;
        return state != null && state.equals(requestState);
    }
    
    /**
     * Business Rule: Check if code is valid for device and location
     */
    public boolean isValidForDevice(String requestIpAddress, String requestUserAgent) {
        // Check IP restrictions for high security codes
        if (securityLevel.enforcesIpRestrictions() && ipAddress != null) {
            if (!isIpAddressValid(requestIpAddress)) return false;
        }
        
        // Check user agent for suspicious changes
        if (userAgent != null && requestUserAgent != null) {
            if (!isUserAgentSimilar(requestUserAgent)) return false;
        }
        
        return true;
    }
    
    /**
     * Business Calculation: Get security score (0-100)
     */
    public int getSecurityScore() {
        int score = 0;
        
        // Base score from security level
        score += securityLevel.getLevel() * 15;
        
        // PKCE adds significant security
        if (isPkceEnabled()) score += 25;
        
        // MFA adds security
        if (multiFactorAuthenticated) score += 20;
        
        // Device fingerprinting adds security
        if (deviceFingerprint != null) score += 10;
        
        // IP restrictions add security
        if (ipAddress != null) score += 10;
        
        // Short expiration adds security
        Duration remaining = getRemainingTime();
        if (remaining.toMinutes() <= 5) score += 15;
        else if (remaining.toMinutes() <= 10) score += 10;
        else if (remaining.toMinutes() <= 30) score += 5;
        
        // Limited scopes add security
        if (scopes != null && scopes.size() <= 3) score += 5;
        
        return Math.min(100, score);
    }
    
    /**
     * Business Rule: Check if code is high security
     */
    public boolean isHighSecurity() {
        return getSecurityScore() >= 75;
    }
    
    /**
     * Business Rule: Check if code requires audit logging
     */
    public boolean requiresAuditLogging() {
        return isHighSecurity() || 
               multiFactorAuthenticated ||
               isPkceEnabled() ||
               securityLevel.getLevel() >= SecurityLevel.HIGH.getLevel();
    }
    
    /**
     * Business Logic: Create used version of authorization code
     */
    public OAuth2AuthorizationCode markAsUsed() {
        if (isAlreadyUsed()) {
            throw new IllegalStateException("Authorization code already used");
        }
        if (!isValid()) {
            throw new IllegalStateException("Cannot use invalid authorization code");
        }
        
        return new OAuth2AuthorizationCode(
            this.code, this.clientId, this.userId, this.redirectUri,
            this.scopes, this.state, this.issuedAt, this.expiresAt,
            this.securityLevel, true, LocalDateTime.now(),
            this.codeChallenge, this.codeChallengeMethod,
            this.sessionId, this.ipAddress, this.userAgent, this.deviceFingerprint,
            this.additionalParameters, this.authenticationMethod,
            this.multiFactorAuthenticated, this.usageCount + 1
        );
    }
    
    /**
     * Business Logic: Create enhanced security version
     */
    public OAuth2AuthorizationCode enhanceSecurityLevel(SecurityLevel newLevel, String reason) {
        Map<String, Object> newAdditionalParameters = new HashMap<>(this.additionalParameters);
        newAdditionalParameters.put("security_enhancement_reason", reason);
        newAdditionalParameters.put("enhanced_at", LocalDateTime.now().toString());
        newAdditionalParameters.put("previous_security_level", this.securityLevel.name());
        
        return new OAuth2AuthorizationCode(
            this.code, this.clientId, this.userId, this.redirectUri,
            this.scopes, this.state, this.issuedAt, this.expiresAt,
            newLevel, this.isUsed, this.usedAt,
            this.codeChallenge, this.codeChallengeMethod,
            this.sessionId, this.ipAddress, this.userAgent, this.deviceFingerprint,
            newAdditionalParameters, this.authenticationMethod,
            this.multiFactorAuthenticated, this.usageCount
        );
    }
    
    /**
     * Business Logic: Get formatted scopes string
     */
    public String getFormattedScopes() {
        if (scopes == null || scopes.isEmpty()) return "none";
        return String.join(" ", scopes);
    }
    
    /**
     * Business Logic: Get authorization code classification
     */
    public AuthorizationCodeClassification getClassification() {
        if (isPkceEnabled() && isHighSecurity()) return AuthorizationCodeClassification.HIGH_SECURITY_PKCE;
        if (isPkceEnabled()) return AuthorizationCodeClassification.PKCE_ENABLED;
        if (multiFactorAuthenticated) return AuthorizationCodeClassification.MFA_PROTECTED;
        if (securityLevel.getLevel() >= SecurityLevel.HIGH.getLevel()) return AuthorizationCodeClassification.HIGH_SECURITY;
        return AuthorizationCodeClassification.STANDARD;
    }
    
    /**
     * Business Validation: Comprehensive validation of authorization code
     */
    public boolean isValidForExchange(String requestClientId, String requestRedirectUri, 
                                    String requestCodeVerifier, String requestState) {
        return isValid() &&
               isForClient(requestClientId) &&
               isForRedirectUri(requestRedirectUri) &&
               isForState(requestState) &&
               validatePkceCodeVerifier(requestCodeVerifier);
    }
    
    // Private helper methods for business logic
    private boolean validateScopes() {
        if (scopes == null) return true;
        
        // Check for invalid scope names
        for (String scope : scopes) {
            if (scope == null || scope.trim().isEmpty()) return false;
            // Scopes should only contain valid characters
            if (!scope.matches("[a-zA-Z0-9:._-]+")) return false;
        }
        
        return true;
    }
    
    private boolean validatePkceIfPresent() {
        if (codeChallenge == null && codeChallengeMethod == null) return true;
        
        // If one is present, both must be present
        if (codeChallenge == null || codeChallengeMethod == null) return false;
        
        // Validate challenge method
        if (!"plain".equals(codeChallengeMethod) && !"S256".equals(codeChallengeMethod)) return false;
        
        // Validate challenge format
        if (codeChallenge.length() < 43 || codeChallenge.length() > 128) return false;
        if (!codeChallenge.matches("[A-Za-z0-9._~-]+")) return false;
        
        return true;
    }
    
    private boolean validateSha256Challenge(String codeVerifier) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            String base64Hash = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            return codeChallenge.equals(base64Hash);
        } catch (Exception e) {
            return false;
        }
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
    
    private boolean isUserAgentSimilar(String requestUserAgent) {
        if (userAgent == null) return true;
        
        // Simple similarity check - could be enhanced with fuzzy matching
        String[] originalParts = userAgent.split("\\s+");
        String[] requestParts = requestUserAgent.split("\\s+");
        
        // Check for major browser/OS components
        int matchCount = 0;
        for (String originalPart : originalParts) {
            for (String requestPart : requestParts) {
                if (originalPart.equalsIgnoreCase(requestPart)) {
                    matchCount++;
                    break;
                }
            }
        }
        
        return matchCount >= Math.min(3, originalParts.length / 2);
    }
    
    // Getters (immutable access)
    public String getCode() { return code; }
    public String getClientId() { return clientId; }
    public String getUserId() { return userId; }
    public String getRedirectUri() { return redirectUri; }
    public Set<String> getScopes() { return new HashSet<>(scopes); }
    public String getState() { return state; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public SecurityLevel getSecurityLevel() { return securityLevel; }
    public boolean isUsed() { return isUsed; }
    public LocalDateTime getUsedAt() { return usedAt; }
    public String getCodeChallenge() { return codeChallenge; }
    public String getCodeChallengeMethod() { return codeChallengeMethod; }
    public String getSessionId() { return sessionId; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public String getDeviceFingerprint() { return deviceFingerprint; }
    public Map<String, Object> getAdditionalParameters() { return new HashMap<>(additionalParameters); }
    public String getAuthenticationMethod() { return authenticationMethod; }
    public boolean isMultiFactorAuthenticated() { return multiFactorAuthenticated; }
    public int getUsageCount() { return usageCount; }
    
    /**
     * Get specific additional parameter
     */
    public Object getAdditionalParameter(String parameterName) {
        return additionalParameters.get(parameterName);
    }
    
    public String getStringParameter(String parameterName, String defaultValue) {
        Object parameter = getAdditionalParameter(parameterName);
        return parameter != null ? parameter.toString() : defaultValue;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OAuth2AuthorizationCode)) return false;
        OAuth2AuthorizationCode that = (OAuth2AuthorizationCode) o;
        return Objects.equals(code, that.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    
    @Override
    public String toString() {
        return String.format("OAuth2AuthorizationCode{code='%s...', clientId='%s', userId='%s', scopes=%s, pkce=%s}", 
                           code.substring(0, Math.min(8, code.length())), clientId, userId, 
                           getFormattedScopes(), isPkceEnabled());
    }
    
    // Static factory methods for common authorization code types
    public static OAuth2AuthorizationCode createStandardCode(String clientId, String userId, String redirectUri,
                                                            Set<String> scopes, Duration expiration) {
        String code = generateAuthorizationCode();
        return new OAuth2AuthorizationCode(code, clientId, userId, redirectUri, scopes, expiration);
    }
    
    public static OAuth2AuthorizationCode createPkceCode(String clientId, String userId, String redirectUri,
                                                        Set<String> scopes, String codeChallenge, String codeChallengeMethod,
                                                        Duration expiration) {
        String code = generateAuthorizationCode();
        LocalDateTime now = LocalDateTime.now();
        
        return new OAuth2AuthorizationCode(
            code, clientId, userId, redirectUri, scopes, null,
            now, now.plus(expiration), SecurityLevel.HIGH, false, null,
            codeChallenge, codeChallengeMethod, null, null, null, null,
            new HashMap<>(), "authorization_code", false, 0
        );
    }
    
    public static OAuth2AuthorizationCode createHighSecurityCode(String clientId, String userId, String redirectUri,
                                                               Set<String> scopes, String ipAddress, String deviceFingerprint,
                                                               boolean mfaAuthenticated, Duration expiration) {
        String code = generateAuthorizationCode();
        LocalDateTime now = LocalDateTime.now();
        
        return new OAuth2AuthorizationCode(
            code, clientId, userId, redirectUri, scopes, null,
            now, now.plus(expiration), SecurityLevel.HIGH, false, null,
            null, null, null, ipAddress, null, deviceFingerprint,
            new HashMap<>(), "authorization_code", mfaAuthenticated, 0
        );
    }
    
    private static String generateAuthorizationCode() {
        // Generate cryptographically secure authorization code
        java.security.SecureRandom random = new java.security.SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    // Builder pattern for complex authorization code creation
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String code;
        private String clientId;
        private String userId;
        private String redirectUri;
        private Set<String> scopes = new HashSet<>();
        private String state;
        private LocalDateTime issuedAt = LocalDateTime.now();
        private LocalDateTime expiresAt;
        private SecurityLevel securityLevel = SecurityLevel.STANDARD;
        private boolean isUsed = false;
        private LocalDateTime usedAt;
        private String codeChallenge;
        private String codeChallengeMethod;
        private String sessionId;
        private String ipAddress;
        private String userAgent;
        private String deviceFingerprint;
        private Map<String, Object> additionalParameters = new HashMap<>();
        private String authenticationMethod = "authorization_code";
        private boolean multiFactorAuthenticated = false;
        private int usageCount = 0;
        
        public Builder code(String code) { this.code = code; return this; }
        public Builder clientId(String clientId) { this.clientId = clientId; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder redirectUri(String redirectUri) { this.redirectUri = redirectUri; return this; }
        public Builder scopes(Set<String> scopes) { this.scopes = scopes; return this; }
        public Builder state(String state) { this.state = state; return this; }
        public Builder issuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; return this; }
        public Builder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public Builder securityLevel(SecurityLevel securityLevel) { this.securityLevel = securityLevel; return this; }
        public Builder isUsed(boolean isUsed) { this.isUsed = isUsed; return this; }
        public Builder usedAt(LocalDateTime usedAt) { this.usedAt = usedAt; return this; }
        public Builder codeChallenge(String codeChallenge) { this.codeChallenge = codeChallenge; return this; }
        public Builder codeChallengeMethod(String codeChallengeMethod) { this.codeChallengeMethod = codeChallengeMethod; return this; }
        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public Builder deviceFingerprint(String deviceFingerprint) { this.deviceFingerprint = deviceFingerprint; return this; }
        public Builder additionalParameters(Map<String, Object> additionalParameters) { this.additionalParameters = additionalParameters; return this; }
        public Builder authenticationMethod(String authenticationMethod) { this.authenticationMethod = authenticationMethod; return this; }
        public Builder multiFactorAuthenticated(boolean multiFactorAuthenticated) { this.multiFactorAuthenticated = multiFactorAuthenticated; return this; }
        public Builder usageCount(int usageCount) { this.usageCount = usageCount; return this; }
        
        public Builder addScope(String scope) { this.scopes.add(scope); return this; }
        public Builder addParameter(String key, Object value) { this.additionalParameters.put(key, value); return this; }
        public Builder expiresInMinutes(long minutes) { 
            this.expiresAt = this.issuedAt.plusMinutes(minutes); 
            return this; 
        }
        public Builder expiresInSeconds(long seconds) { 
            this.expiresAt = this.issuedAt.plusSeconds(seconds); 
            return this; 
        }
        public Builder pkce(String codeChallenge, String codeChallengeMethod) {
            this.codeChallenge = codeChallenge;
            this.codeChallengeMethod = codeChallengeMethod;
            return this;
        }
        
        public OAuth2AuthorizationCode build() {
            if (code == null) {
                code = generateAuthorizationCode();
            }
            if (expiresAt == null) {
                expiresAt = issuedAt.plusMinutes(10); // Default 10 minutes
            }
            
            return new OAuth2AuthorizationCode(code, clientId, userId, redirectUri, scopes, state,
                                             issuedAt, expiresAt, securityLevel, isUsed, usedAt,
                                             codeChallenge, codeChallengeMethod, sessionId, ipAddress, userAgent,
                                             deviceFingerprint, additionalParameters, authenticationMethod,
                                             multiFactorAuthenticated, usageCount);
        }
    }
}

enum AuthorizationCodeClassification {
    STANDARD, HIGH_SECURITY, PKCE_ENABLED, MFA_PROTECTED, HIGH_SECURITY_PKCE
}