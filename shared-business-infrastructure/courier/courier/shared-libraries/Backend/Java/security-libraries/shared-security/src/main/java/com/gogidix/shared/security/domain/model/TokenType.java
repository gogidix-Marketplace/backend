package com.gogidix.shared.security.domain.model;

/**
 * Enumeration of security token types
 */
public enum TokenType {
    
    /** Access token for API authentication */
    ACCESS_TOKEN("Access Token"),
    
    /** Refresh token for obtaining new access tokens */
    REFRESH_TOKEN("Refresh Token"),
    
    /** API key token for service-to-service authentication */
    API_TOKEN("API Token"),
    
    /** Session token for web session management */
    SESSION_TOKEN("Session Token"),
    
    /** Temporary token for password reset */
    PASSWORD_RESET_TOKEN("Password Reset Token"),
    
    /** Token for email verification */
    EMAIL_VERIFICATION_TOKEN("Email Verification Token"),
    
    /** Token for phone verification */
    PHONE_VERIFICATION_TOKEN("Phone Verification Token"),
    
    /** Token for two-factor authentication */
    TWO_FACTOR_TOKEN("Two Factor Token"),
    
    /** Token for single sign-on */
    SSO_TOKEN("SSO Token"),
    
    /** Token for service-to-service communication */
    SERVICE_TOKEN("Service Token"),
    
    /** Token for device authentication */
    DEVICE_TOKEN("Device Token"),
    
    /** Token for webhook authentication */
    WEBHOOK_TOKEN("Webhook Token");
    
    private final String displayName;
    
    TokenType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Determines if this token type requires user interaction
     */
    public boolean requiresUserInteraction() {
        return this == PASSWORD_RESET_TOKEN ||
               this == EMAIL_VERIFICATION_TOKEN ||
               this == PHONE_VERIFICATION_TOKEN ||
               this == TWO_FACTOR_TOKEN;
    }
    
    /**
     * Determines if this token type can be refreshed
     */
    public boolean canBeRefreshed() {
        return this == ACCESS_TOKEN || this == SESSION_TOKEN;
    }
    
    /**
     * Determines if this token type is for authentication
     */
    public boolean isAuthenticationToken() {
        return this == ACCESS_TOKEN ||
               this == SESSION_TOKEN ||
               this == SSO_TOKEN ||
               this == TWO_FACTOR_TOKEN;
    }
    
    /**
     * Determines if this token type is for authorization
     */
    public boolean isAuthorizationToken() {
        return this == API_TOKEN ||
               this == SERVICE_TOKEN ||
               this == WEBHOOK_TOKEN;
    }
    
    /**
     * Determines if this token type is temporary
     */
    public boolean isTemporary() {
        return this == PASSWORD_RESET_TOKEN ||
               this == EMAIL_VERIFICATION_TOKEN ||
               this == PHONE_VERIFICATION_TOKEN ||
               this == TWO_FACTOR_TOKEN;
    }
    
    /**
     * Gets the default expiration time in seconds for this token type
     */
    public long getDefaultExpirationSeconds() {
        return switch (this) {
            case ACCESS_TOKEN -> 3600; // 1 hour
            case REFRESH_TOKEN -> 2592000; // 30 days
            case API_TOKEN -> 31536000; // 1 year
            case SESSION_TOKEN -> 86400; // 24 hours
            case PASSWORD_RESET_TOKEN -> 1800; // 30 minutes
            case EMAIL_VERIFICATION_TOKEN -> 86400; // 24 hours
            case PHONE_VERIFICATION_TOKEN -> 300; // 5 minutes
            case TWO_FACTOR_TOKEN -> 300; // 5 minutes
            case SSO_TOKEN -> 28800; // 8 hours
            case SERVICE_TOKEN -> 86400; // 24 hours
            case DEVICE_TOKEN -> 2592000; // 30 days
            case WEBHOOK_TOKEN -> 31536000; // 1 year
        };
    }
    
    /**
     * Gets the security level required for this token type
     */
    public SecurityLevel getRequiredSecurityLevel() {
        return switch (this) {
            case ACCESS_TOKEN, SESSION_TOKEN, SSO_TOKEN -> SecurityLevel.STANDARD;
            case API_TOKEN, SERVICE_TOKEN, WEBHOOK_TOKEN -> SecurityLevel.HIGH;
            case REFRESH_TOKEN, DEVICE_TOKEN -> SecurityLevel.HIGH;
            case PASSWORD_RESET_TOKEN, EMAIL_VERIFICATION_TOKEN, 
                 PHONE_VERIFICATION_TOKEN, TWO_FACTOR_TOKEN -> SecurityLevel.CRITICAL;
        };
    }
}