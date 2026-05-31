package com.gogidix.shared.security.domain.model;

/**
 * Enumeration of security levels for tokens and operations
 */
public enum SecurityLevel {
    
    /** Basic security level for public operations */
    BASIC("Basic", 1),
    
    /** Standard security level for regular authenticated operations */
    STANDARD("Standard", 2),
    
    /** High security level for sensitive operations */
    HIGH("High", 3),
    
    /** Critical security level for highly sensitive operations */
    CRITICAL("Critical", 4),
    
    /** Maximum security level for administrative operations */
    MAXIMUM("Maximum", 5);
    
    private final String displayName;
    private final int level;
    
    SecurityLevel(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getLevel() {
        return level;
    }
    
    /**
     * Determines if this security level meets the minimum required level
     */
    public boolean meetsMinimumLevel(SecurityLevel minimumRequired) {
        return this.level >= minimumRequired.level;
    }
    
    /**
     * Determines if this security level is higher than the other level
     */
    public boolean isHigherThan(SecurityLevel other) {
        return this.level > other.level;
    }
    
    /**
     * Determines if this security level requires multi-factor authentication
     */
    public boolean requiresMultiFactorAuth() {
        return this.level >= HIGH.level;
    }
    
    /**
     * Determines if this security level requires additional verification
     */
    public boolean requiresAdditionalVerification() {
        return this.level >= CRITICAL.level;
    }
    
    /**
     * Determines if this security level requires admin approval
     */
    public boolean requiresAdminApproval() {
        return this.level >= MAXIMUM.level;
    }
    
    /**
     * Gets the session timeout in seconds for this security level
     */
    public long getSessionTimeoutSeconds() {
        return switch (this) {
            case BASIC -> 86400; // 24 hours
            case STANDARD -> 28800; // 8 hours
            case HIGH -> 14400; // 4 hours
            case CRITICAL -> 3600; // 1 hour
            case MAXIMUM -> 1800; // 30 minutes
        };
    }
    
    /**
     * Gets the maximum allowed concurrent sessions for this security level
     */
    public int getMaxConcurrentSessions() {
        return switch (this) {
            case BASIC -> 10;
            case STANDARD -> 5;
            case HIGH -> 3;
            case CRITICAL -> 2;
            case MAXIMUM -> 1;
        };
    }
    
    /**
     * Gets the required password strength for this security level
     */
    public int getRequiredPasswordStrength() {
        return switch (this) {
            case BASIC -> 6; // Minimum 6 characters
            case STANDARD -> 8; // Minimum 8 characters
            case HIGH -> 10; // Minimum 10 characters with complexity
            case CRITICAL -> 12; // Minimum 12 characters with high complexity
            case MAXIMUM -> 16; // Minimum 16 characters with maximum complexity
        };
    }
    
    /**
     * Determines if IP address restrictions should be enforced
     */
    public boolean enforcesIpRestrictions() {
        return this.level >= HIGH.level;
    }
    
    /**
     * Determines if device fingerprinting is required
     */
    public boolean requiresDeviceFingerprinting() {
        return this.level >= CRITICAL.level;
    }
    
    /**
     * Gets the audit logging level required
     */
    public String getAuditLoggingLevel() {
        return switch (this) {
            case BASIC -> "MINIMAL";
            case STANDARD -> "STANDARD";
            case HIGH -> "DETAILED";
            case CRITICAL -> "COMPREHENSIVE";
            case MAXIMUM -> "FORENSIC";
        };
    }
    
    /**
     * Gets the encryption strength required for this security level
     */
    public String getEncryptionStrength() {
        return switch (this) {
            case BASIC -> "AES-128";
            case STANDARD -> "AES-256";
            case HIGH -> "AES-256-GCM";
            case CRITICAL -> "AES-256-GCM + RSA-4096";
            case MAXIMUM -> "AES-256-GCM + RSA-4096 + HSM";
        };
    }
}