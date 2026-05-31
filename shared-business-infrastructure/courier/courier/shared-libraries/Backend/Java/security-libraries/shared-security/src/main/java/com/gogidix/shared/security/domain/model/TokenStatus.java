package com.gogidix.shared.security.domain.model;

/**
 * Enumeration of security token statuses
 */
public enum TokenStatus {
    
    /** Token is active and can be used */
    ACTIVE("Active"),
    
    /** Token has been revoked and cannot be used */
    REVOKED("Revoked"),
    
    /** Token is temporarily suspended */
    SUSPENDED("Suspended"),
    
    /** Token has expired */
    EXPIRED("Expired"),
    
    /** Token is pending activation */
    PENDING("Pending"),
    
    /** Token has been used and is no longer valid (for one-time tokens) */
    CONSUMED("Consumed"),
    
    /** Token has been replaced by a new token */
    REPLACED("Replaced"),

    /** Token has been compromised and must not be used */
    COMPROMISED("Compromised");
    
    private final String displayName;
    
    TokenStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Determines if this status indicates the token is usable
     */
    public boolean isUsable() {
        return this == ACTIVE;
    }
    
    /**
     * Determines if this status indicates the token is permanently unusable
     */
    public boolean isPermanentlyUnusable() {
        return this == REVOKED || this == EXPIRED || this == CONSUMED || this == REPLACED || this == COMPROMISED;
    }
    
    /**
     * Determines if this status can be changed to active
     */
    public boolean canBeActivated() {
        return this == PENDING || this == SUSPENDED;
    }
    
    /**
     * Determines if this status represents a security concern
     */
    public boolean isSecurityConcern() {
        return this == REVOKED || this == SUSPENDED || this == COMPROMISED;
    }
    
    /**
     * Gets the next expected status in the token lifecycle
     */
    public TokenStatus getNextExpectedStatus() {
        return switch (this) {
            case PENDING -> ACTIVE;
            case ACTIVE -> EXPIRED; // Natural progression
            case SUSPENDED -> ACTIVE; // Can be reactivated
            default -> this; // Final states remain unchanged
        };
    }
    
    /**
     * Determines if transition to the target status is allowed
     */
    public boolean canTransitionTo(TokenStatus targetStatus) {
        return switch (this) {
            case PENDING -> targetStatus == ACTIVE || 
                           targetStatus == REVOKED || 
                           targetStatus == EXPIRED;
            
            case ACTIVE -> targetStatus == REVOKED || 
                          targetStatus == SUSPENDED || 
                          targetStatus == EXPIRED ||
                          targetStatus == CONSUMED ||
                          targetStatus == REPLACED;
            
            case SUSPENDED -> targetStatus == ACTIVE || 
                             targetStatus == REVOKED || 
                             targetStatus == EXPIRED;
            
            case EXPIRED, REVOKED, CONSUMED, REPLACED, COMPROMISED -> false; // Final states
        };
    }
}