package com.gogidix.shared.security.domain.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Set;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Rich Domain Model for JWT Security Context - HEXAGONAL ARCHITECTURE TEMPLATE
 * 
 * This serves as the JWT-specific security context for Agent B services.
 * Demonstrates:
 * - JWT-specific business logic and validation
 * - Zero infrastructure dependencies (NO Lombok, JPA, etc.)
 * - Immutable design with builder pattern
 * - JWT claim validation and management
 * - Security policy enforcement
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B JWT Security Context
 * @version 1.0.0
 */
public class JwtSecurityContext {
    
    // JWT Core Claims (immutable)
    private final String subject; // sub
    private final String issuer; // iss
    private final Set<String> audience; // aud
    private final LocalDateTime issuedAt; // iat
    private final LocalDateTime expiresAt; // exp
    private final LocalDateTime notBefore; // nbf
    private final String jwtId; // jti
    
    // Security Claims (immutable)
    private final Set<String> roles;
    private final Set<String> permissions;
    private final Set<String> authorities;
    private final SecurityLevel securityLevel;
    private final String sessionId;
    
    // Authentication Claims (immutable)
    private final String authenticationMethod;
    private final LocalDateTime authenticationTime;
    private final boolean multiFactorAuthenticated;
    private final String mfaMethod;
    
    // Context Claims (immutable)
    private final String clientId;
    private final String deviceId;
    private final String ipAddress;
    private final String userAgent;
    private final String geolocation;
    
    // Custom Claims (immutable)
    private final Map<String, Object> customClaims;
    
    // Constructor
    public JwtSecurityContext(String subject, String issuer, Set<String> audience, 
                             LocalDateTime issuedAt, LocalDateTime expiresAt,
                             Set<String> roles, Set<String> permissions,
                             SecurityLevel securityLevel, String sessionId) {
        this.subject = Objects.requireNonNull(subject, "Subject cannot be null");
        this.issuer = Objects.requireNonNull(issuer, "Issuer cannot be null");
        this.audience = audience != null ? new HashSet<>(audience) : new HashSet<>();
        this.issuedAt = Objects.requireNonNull(issuedAt, "Issued at cannot be null");
        this.expiresAt = expiresAt;
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
        this.permissions = permissions != null ? new HashSet<>(permissions) : new HashSet<>();
        this.securityLevel = securityLevel != null ? securityLevel : SecurityLevel.STANDARD;
        this.sessionId = sessionId;
        
        // Defaults
        this.notBefore = issuedAt;
        this.jwtId = java.util.UUID.randomUUID().toString();
        this.authorities = new HashSet<>();
        this.authenticationMethod = "jwt";
        this.authenticationTime = issuedAt;
        this.multiFactorAuthenticated = false;
        this.mfaMethod = null;
        this.clientId = null;
        this.deviceId = null;
        this.ipAddress = null;
        this.userAgent = null;
        this.geolocation = null;
        this.customClaims = new HashMap<>();
    }
    
    // Full constructor for complete context creation
    public JwtSecurityContext(String subject, String issuer, Set<String> audience,
                             LocalDateTime issuedAt, LocalDateTime expiresAt, LocalDateTime notBefore, String jwtId,
                             Set<String> roles, Set<String> permissions, Set<String> authorities,
                             SecurityLevel securityLevel, String sessionId,
                             String authenticationMethod, LocalDateTime authenticationTime,
                             boolean multiFactorAuthenticated, String mfaMethod,
                             String clientId, String deviceId, String ipAddress, String userAgent, String geolocation,
                             Map<String, Object> customClaims) {
        this.subject = subject;
        this.issuer = issuer;
        this.audience = audience != null ? new HashSet<>(audience) : new HashSet<>();
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.notBefore = notBefore;
        this.jwtId = jwtId;
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
        this.permissions = permissions != null ? new HashSet<>(permissions) : new HashSet<>();
        this.authorities = authorities != null ? new HashSet<>(authorities) : new HashSet<>();
        this.securityLevel = securityLevel;
        this.sessionId = sessionId;
        this.authenticationMethod = authenticationMethod;
        this.authenticationTime = authenticationTime;
        this.multiFactorAuthenticated = multiFactorAuthenticated;
        this.mfaMethod = mfaMethod;
        this.clientId = clientId;
        this.deviceId = deviceId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.geolocation = geolocation;
        this.customClaims = customClaims != null ? new HashMap<>(customClaims) : new HashMap<>();
    }
    
    // ==============================================
    // JWT BUSINESS LOGIC METHODS - Template
    // ==============================================
    
    /**
     * Business Rule: Check if JWT is currently valid
     */
    public boolean isValid() {
        return subject != null && !subject.trim().isEmpty() &&
               issuer != null && !issuer.trim().isEmpty() &&
               issuedAt != null &&
               !isExpired() &&
               !isNotYetValid() &&
               hasValidStructure();
    }
    
    /**
     * Business Rule: Check if JWT has expired
     */
    public boolean isExpired() {
        if (expiresAt == null) return false;
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * Business Rule: Check if JWT is not yet valid (nbf check)
     */
    public boolean isNotYetValid() {
        if (notBefore == null) return false;
        return LocalDateTime.now().isBefore(notBefore);
    }
    
    /**
     * Business Rule: Check if JWT has valid structure
     */
    public boolean hasValidStructure() {
        return subject != null && !subject.trim().isEmpty() &&
               issuer != null && !issuer.trim().isEmpty() &&
               jwtId != null && !jwtId.trim().isEmpty() &&
               issuedAt != null &&
               validateTimestamps() &&
               validateRolesAndPermissions();
    }
    
    /**
     * Business Rule: Check if user has specific role
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
    
    /**
     * Business Rule: Check if user has any of the required roles
     */
    public boolean hasAnyRole(Set<String> requiredRoles) {
        if (roles == null || requiredRoles == null) return false;
        return roles.stream().anyMatch(requiredRoles::contains);
    }
    
    /**
     * Business Rule: Check if user has all required roles
     */
    public boolean hasAllRoles(Set<String> requiredRoles) {
        if (roles == null || requiredRoles == null) return false;
        return roles.containsAll(requiredRoles);
    }
    
    /**
     * Business Rule: Check if user has specific permission
     */
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }
    
    /**
     * Business Rule: Check if user has any of the required permissions
     */
    public boolean hasAnyPermission(Set<String> requiredPermissions) {
        if (permissions == null || requiredPermissions == null) return false;
        return permissions.stream().anyMatch(requiredPermissions::contains);
    }
    
    /**
     * Business Rule: Check if user has authority
     */
    public boolean hasAuthority(String authority) {
        return authorities != null && authorities.contains(authority);
    }
    
    /**
     * Business Rule: Check if JWT is for specific audience
     */
    public boolean isForAudience(String targetAudience) {
        return audience != null && audience.contains(targetAudience);
    }
    
    /**
     * Business Rule: Check if JWT is for any of the target audiences
     */
    public boolean isForAnyAudience(Set<String> targetAudiences) {
        if (audience == null || targetAudiences == null) return false;
        return audience.stream().anyMatch(targetAudiences::contains);
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
     * Business Rule: Check if JWT will expire soon
     */
    public boolean willExpireSoon(Duration threshold) {
        Duration remaining = getRemainingTime();
        return remaining.compareTo(threshold) <= 0 && !remaining.isZero();
    }
    
    /**
     * Business Calculation: Get age of JWT
     */
    public Duration getAge() {
        if (issuedAt == null) return Duration.ZERO;
        return Duration.between(issuedAt, LocalDateTime.now());
    }
    
    /**
     * Business Rule: Check if JWT is fresh (recently issued)
     */
    public boolean isFresh(Duration freshnessThreshold) {
        return getAge().compareTo(freshnessThreshold) <= 0;
    }
    
    /**
     * Business Rule: Check if JWT requires MFA for operation
     */
    public boolean requiresMfaForOperation(String operation) {
        if (multiFactorAuthenticated) return false;
        
        // High security operations always require MFA
        if (securityLevel.requiresMultiFactorAuth()) return true;
        
        // Specific sensitive operations
        Set<String> mfaRequiredOperations = Set.of(
            "CHANGE_PASSWORD", "MODIFY_SECURITY_SETTINGS", "DELETE_ACCOUNT",
            "TRANSFER_FUNDS", "ACCESS_SENSITIVE_DATA", "ADMIN_OPERATION"
        );
        
        return mfaRequiredOperations.contains(operation.toUpperCase());
    }
    
    /**
     * Business Rule: Check if context is valid for device and location
     */
    public boolean isValidForDevice(String requestDeviceId, String requestIpAddress) {
        // Check device restrictions for high security
        if (securityLevel.requiresDeviceFingerprinting() && deviceId != null) {
            if (!deviceId.equals(requestDeviceId)) return false;
        }
        
        // Check IP restrictions for high security
        if (securityLevel.enforcesIpRestrictions() && ipAddress != null) {
            return isIpAddressValid(requestIpAddress);
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
        
        // MFA adds security
        if (multiFactorAuthenticated) score += 20;
        
        // Device restrictions add security
        if (deviceId != null) score += 10;
        
        // IP restrictions add security
        if (ipAddress != null) score += 10;
        
        // Short expiration adds security
        Duration remaining = getRemainingTime();
        if (remaining.toHours() <= 1) score += 15;
        else if (remaining.toHours() <= 8) score += 10;
        else if (remaining.toHours() <= 24) score += 5;
        
        // Multiple roles/permissions add complexity
        if (roles != null && roles.size() > 1) score += 5;
        if (permissions != null && permissions.size() > 3) score += 5;
        
        return Math.min(100, score);
    }
    
    /**
     * Business Rule: Check if context is high security
     */
    public boolean isHighSecurity() {
        return getSecurityScore() >= 75;
    }
    
    /**
     * Business Rule: Check if context requires audit logging
     */
    public boolean requiresAuditLogging() {
        return isHighSecurity() || 
               multiFactorAuthenticated ||
               securityLevel.getLevel() >= SecurityLevel.HIGH.getLevel();
    }
    
    /**
     * Business Rule: Check if context supports operation
     */
    public boolean supportsOperation(String operation, Set<String> requiredRoles, Set<String> requiredPermissions) {
        if (requiredRoles != null && !hasAnyRole(requiredRoles)) return false;
        if (requiredPermissions != null && !hasAnyPermission(requiredPermissions)) return false;
        
        // Check if MFA is required for this operation
        if (requiresMfaForOperation(operation)) return false;
        
        return isValid();
    }
    
    /**
     * Business Logic: Get combined authorities (roles + permissions)
     */
    public Set<String> getCombinedAuthorities() {
        Set<String> combined = new HashSet<>();
        if (roles != null) {
            roles.forEach(role -> combined.add("ROLE_" + role));
        }
        if (permissions != null) {
            combined.addAll(permissions);
        }
        if (authorities != null) {
            combined.addAll(authorities);
        }
        return combined;
    }
    
    /**
     * Business Logic: Get JWT claims as Map (standard + custom)
     */
    public Map<String, Object> getAllClaims() {
        Map<String, Object> claims = new HashMap<>();
        
        // Standard JWT claims
        claims.put("sub", subject);
        claims.put("iss", issuer);
        claims.put("aud", audience);
        claims.put("iat", issuedAt.toString());
        if (expiresAt != null) claims.put("exp", expiresAt.toString());
        if (notBefore != null) claims.put("nbf", notBefore.toString());
        claims.put("jti", jwtId);
        
        // Security claims
        if (!roles.isEmpty()) claims.put("roles", roles);
        if (!permissions.isEmpty()) claims.put("permissions", permissions);
        if (!authorities.isEmpty()) claims.put("authorities", authorities);
        claims.put("security_level", securityLevel.name());
        if (sessionId != null) claims.put("session_id", sessionId);
        
        // Authentication claims
        claims.put("auth_method", authenticationMethod);
        if (authenticationTime != null) claims.put("auth_time", authenticationTime.toString());
        claims.put("mfa", multiFactorAuthenticated);
        if (mfaMethod != null) claims.put("mfa_method", mfaMethod);
        
        // Context claims
        if (clientId != null) claims.put("client_id", clientId);
        if (deviceId != null) claims.put("device_id", deviceId);
        if (ipAddress != null) claims.put("ip_address", ipAddress);
        if (userAgent != null) claims.put("user_agent", userAgent);
        if (geolocation != null) claims.put("geolocation", geolocation);
        
        // Custom claims
        claims.putAll(customClaims);
        
        return claims;
    }
    
    /**
     * Business Logic: Create enhanced security version
     */
    public JwtSecurityContext enhanceSecurityLevel(SecurityLevel newLevel, String reason) {
        Map<String, Object> newCustomClaims = new HashMap<>(this.customClaims);
        newCustomClaims.put("security_enhancement_reason", reason);
        newCustomClaims.put("enhanced_at", LocalDateTime.now().toString());
        newCustomClaims.put("previous_security_level", this.securityLevel.name());
        
        return new JwtSecurityContext(
            this.subject, this.issuer, this.audience,
            this.issuedAt, this.expiresAt, this.notBefore, this.jwtId,
            this.roles, this.permissions, this.authorities,
            newLevel, this.sessionId,
            this.authenticationMethod, this.authenticationTime,
            this.multiFactorAuthenticated, this.mfaMethod,
            this.clientId, this.deviceId, this.ipAddress, this.userAgent, this.geolocation,
            newCustomClaims
        );
    }
    
    /**
     * Business Logic: Create context with additional roles
     */
    public JwtSecurityContext withAdditionalRoles(Set<String> additionalRoles) {
        Set<String> newRoles = new HashSet<>(this.roles);
        newRoles.addAll(additionalRoles);
        
        return new JwtSecurityContext(
            this.subject, this.issuer, this.audience,
            this.issuedAt, this.expiresAt, this.notBefore, this.jwtId,
            newRoles, this.permissions, this.authorities,
            this.securityLevel, this.sessionId,
            this.authenticationMethod, this.authenticationTime,
            this.multiFactorAuthenticated, this.mfaMethod,
            this.clientId, this.deviceId, this.ipAddress, this.userAgent, this.geolocation,
            this.customClaims
        );
    }
    
    /**
     * Business Logic: Create context with MFA enabled
     */
    public JwtSecurityContext withMfaAuthentication(String method) {
        Map<String, Object> newCustomClaims = new HashMap<>(this.customClaims);
        newCustomClaims.put("mfa_enabled_at", LocalDateTime.now().toString());
        
        return new JwtSecurityContext(
            this.subject, this.issuer, this.audience,
            this.issuedAt, this.expiresAt, this.notBefore, this.jwtId,
            this.roles, this.permissions, this.authorities,
            this.securityLevel, this.sessionId,
            this.authenticationMethod, this.authenticationTime,
            true, method,
            this.clientId, this.deviceId, this.ipAddress, this.userAgent, this.geolocation,
            newCustomClaims
        );
    }
    
    // Private helper methods
    private boolean validateTimestamps() {
        // Issued at must be before expires at
        if (expiresAt != null && issuedAt.isAfter(expiresAt)) return false;
        
        // Not before must be before expires at
        if (notBefore != null && expiresAt != null && notBefore.isAfter(expiresAt)) return false;
        
        return true;
    }
    
    private boolean validateRolesAndPermissions() {
        // Check for invalid role names
        if (roles != null) {
            for (String role : roles) {
                if (role == null || role.trim().isEmpty()) return false;
            }
        }
        
        // Check for invalid permission names
        if (permissions != null) {
            for (String permission : permissions) {
                if (permission == null || permission.trim().isEmpty()) return false;
            }
        }
        
        return true;
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
    
    // Getters (immutable access)
    public String getSubject() { return subject; }
    public String getIssuer() { return issuer; }
    public Set<String> getAudience() { return new HashSet<>(audience); }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getNotBefore() { return notBefore; }
    public String getJwtId() { return jwtId; }
    public Set<String> getRoles() { return new HashSet<>(roles); }
    public Set<String> getPermissions() { return new HashSet<>(permissions); }
    public Set<String> getAuthorities() { return new HashSet<>(authorities); }
    public SecurityLevel getSecurityLevel() { return securityLevel; }
    public String getSessionId() { return sessionId; }
    public String getAuthenticationMethod() { return authenticationMethod; }
    public LocalDateTime getAuthenticationTime() { return authenticationTime; }
    public boolean isMultiFactorAuthenticated() { return multiFactorAuthenticated; }
    public String getMfaMethod() { return mfaMethod; }
    public String getClientId() { return clientId; }
    public String getDeviceId() { return deviceId; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public String getGeolocation() { return geolocation; }
    public Map<String, Object> getCustomClaims() { return new HashMap<>(customClaims); }
    
    /**
     * Get specific custom claim
     */
    public Object getCustomClaim(String claimName) {
        return customClaims.get(claimName);
    }
    
    public String getStringClaim(String claimName, String defaultValue) {
        Object claim = getCustomClaim(claimName);
        return claim != null ? claim.toString() : defaultValue;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtSecurityContext)) return false;
        JwtSecurityContext that = (JwtSecurityContext) o;
        return Objects.equals(jwtId, that.jwtId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(jwtId);
    }
    
    @Override
    public String toString() {
        return String.format("JwtSecurityContext{subject='%s', issuer='%s', securityLevel=%s, mfa=%s}", 
                           subject, issuer, securityLevel, multiFactorAuthenticated);
    }
    
    // Static factory methods for common JWT contexts
    public static JwtSecurityContext createStandardJwt(String subject, String issuer, 
                                                      Set<String> roles, Duration expiration) {
        LocalDateTime now = LocalDateTime.now();
        return new JwtSecurityContext(
            subject, issuer, Set.of("gogidx-platform"),
            now, now.plus(expiration), roles, new HashSet<>(),
            SecurityLevel.STANDARD, null
        );
    }
    
    public static JwtSecurityContext createHighSecurityJwt(String subject, String issuer,
                                                          Set<String> roles, Set<String> permissions,
                                                          Duration expiration, String deviceId, String ipAddress) {
        LocalDateTime now = LocalDateTime.now();
        return new JwtSecurityContext(
            subject, issuer, Set.of("gogidx-platform"),
            now, now, now.plus(expiration), java.util.UUID.randomUUID().toString(),
            roles, permissions, new HashSet<>(),
            SecurityLevel.HIGH, null,
            "jwt", now, true, "totp",
            null, deviceId, ipAddress, null, null,
            new HashMap<>()
        );
    }
    
    // Builder pattern for complex JWT context creation
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String subject;
        private String issuer = "gogidx-auth-service";
        private Set<String> audience = Set.of("gogidx-platform");
        private LocalDateTime issuedAt = LocalDateTime.now();
        private LocalDateTime expiresAt;
        private LocalDateTime notBefore;
        private String jwtId = java.util.UUID.randomUUID().toString();
        private Set<String> roles = new HashSet<>();
        private Set<String> permissions = new HashSet<>();
        private Set<String> authorities = new HashSet<>();
        private SecurityLevel securityLevel = SecurityLevel.STANDARD;
        private String sessionId;
        private String authenticationMethod = "jwt";
        private LocalDateTime authenticationTime = LocalDateTime.now();
        private boolean multiFactorAuthenticated = false;
        private String mfaMethod;
        private String clientId;
        private String deviceId;
        private String ipAddress;
        private String userAgent;
        private String geolocation;
        private Map<String, Object> customClaims = new HashMap<>();
        
        public Builder subject(String subject) { this.subject = subject; return this; }
        public Builder issuer(String issuer) { this.issuer = issuer; return this; }
        public Builder audience(Set<String> audience) { this.audience = audience; return this; }
        public Builder issuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; return this; }
        public Builder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public Builder notBefore(LocalDateTime notBefore) { this.notBefore = notBefore; return this; }
        public Builder jwtId(String jwtId) { this.jwtId = jwtId; return this; }
        public Builder roles(Set<String> roles) { this.roles = roles; return this; }
        public Builder permissions(Set<String> permissions) { this.permissions = permissions; return this; }
        public Builder authorities(Set<String> authorities) { this.authorities = authorities; return this; }
        public Builder securityLevel(SecurityLevel securityLevel) { this.securityLevel = securityLevel; return this; }
        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public Builder authenticationMethod(String authenticationMethod) { this.authenticationMethod = authenticationMethod; return this; }
        public Builder authenticationTime(LocalDateTime authenticationTime) { this.authenticationTime = authenticationTime; return this; }
        public Builder multiFactorAuthenticated(boolean multiFactorAuthenticated) { this.multiFactorAuthenticated = multiFactorAuthenticated; return this; }
        public Builder mfaMethod(String mfaMethod) { this.mfaMethod = mfaMethod; return this; }
        public Builder clientId(String clientId) { this.clientId = clientId; return this; }
        public Builder deviceId(String deviceId) { this.deviceId = deviceId; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public Builder geolocation(String geolocation) { this.geolocation = geolocation; return this; }
        public Builder customClaims(Map<String, Object> customClaims) { this.customClaims = customClaims; return this; }
        
        public Builder addRole(String role) { this.roles.add(role); return this; }
        public Builder addPermission(String permission) { this.permissions.add(permission); return this; }
        public Builder addAuthority(String authority) { this.authorities.add(authority); return this; }
        public Builder addCustomClaim(String key, Object value) { this.customClaims.put(key, value); return this; }
        public Builder addAudience(String audience) { 
            Set<String> newAudience = new HashSet<>(this.audience);
            newAudience.add(audience);
            this.audience = newAudience;
            return this; 
        }
        public Builder expiresInMinutes(long minutes) { 
            this.expiresAt = this.issuedAt.plusMinutes(minutes); 
            return this; 
        }
        public Builder expiresInHours(long hours) { 
            this.expiresAt = this.issuedAt.plusHours(hours); 
            return this; 
        }
        public Builder expiresInDays(long days) { 
            this.expiresAt = this.issuedAt.plusDays(days); 
            return this; 
        }
        
        public JwtSecurityContext build() {
            return new JwtSecurityContext(subject, issuer, audience,
                                        issuedAt, expiresAt, notBefore, jwtId,
                                        roles, permissions, authorities,
                                        securityLevel, sessionId,
                                        authenticationMethod, authenticationTime,
                                        multiFactorAuthenticated, mfaMethod,
                                        clientId, deviceId, ipAddress, userAgent, geolocation,
                                        customClaims);
        }
    }
}