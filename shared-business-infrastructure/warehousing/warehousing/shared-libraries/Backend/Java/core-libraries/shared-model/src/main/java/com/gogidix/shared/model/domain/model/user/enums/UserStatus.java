package com.gogidix.shared.model.domain.model.user.enums;

/**
 * Enumeration of possible user account statuses.
 * Represents the current state of a user account in the system.
 */
public enum UserStatus {
    
    /**
     * User account is active and in good standing.
     */
    ACTIVE("Active", "User account is active and operational"),
    
    /**
     * User account is inactive but not suspended.
     */
    INACTIVE("Inactive", "User account is inactive"),
    
    /**
     * User account is suspended due to policy violations.
     */
    SUSPENDED("Suspended", "User account has been suspended"),
    
    /**
     * User account is pending email or phone verification.
     */
    PENDING_VERIFICATION("Pending Verification", "User account awaits verification"),
    
    /**
     * User account is temporarily locked due to security reasons.
     */
    LOCKED("Locked", "User account is temporarily locked"),
    
    /**
     * User has requested account closure but not yet processed.
     */
    PENDING_CLOSURE("Pending Closure", "User account closure is being processed"),
    
    /**
     * User account is closed and cannot be reactivated.
     */
    CLOSED("Closed", "User account has been permanently closed"),
    
    /**
     * User account has been banned permanently.
     */
    BANNED("Banned", "User account has been permanently banned");
    
    private final String displayName;
    private final String description;
    
    UserStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * Gets the human-readable display name.
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the detailed description of the status.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if the status allows login.
     */
    public boolean allowsLogin() {
        return this == ACTIVE;
    }
    
    /**
     * Checks if the status is temporary (can be changed).
     */
    public boolean isTemporary() {
        return this == PENDING_VERIFICATION || 
               this == LOCKED || 
               this == SUSPENDED || 
               this == PENDING_CLOSURE ||
               this == INACTIVE;
    }
    
    /**
     * Checks if the status is permanent (cannot be easily changed).
     */
    public boolean isPermanent() {
        return this == BANNED || this == CLOSED;
    }
    
    /**
     * Checks if the user can perform transactions.
     */
    public boolean allowsTransactions() {
        return this == ACTIVE;
    }
    
    /**
     * Checks if the account requires administrative attention.
     */
    public boolean requiresAttention() {
        return this == SUSPENDED || 
               this == PENDING_VERIFICATION || 
               this == PENDING_CLOSURE ||
               this == LOCKED;
    }
    
    /**
     * Gets the severity level of the status (0-4, higher is more severe).
     */
    public int getSeverityLevel() {
        return switch (this) {
            case ACTIVE, INACTIVE -> 0;
            case PENDING_VERIFICATION -> 1;
            case LOCKED -> 2;
            case SUSPENDED, PENDING_CLOSURE -> 3;
            case CLOSED, BANNED -> 4;
        };
    }
    
    /**
     * Checks if transition to another status is allowed.
     */
    public boolean canTransitionTo(UserStatus newStatus) {
        // Cannot transition from permanent states
        if (this.isPermanent() && newStatus != this) {
            return false;
        }
        
        // Specific transition rules
        return switch (this) {
            case ACTIVE -> newStatus != PENDING_VERIFICATION;
            case INACTIVE -> true; // Can transition to any status
            case PENDING_VERIFICATION -> newStatus == ACTIVE || newStatus == CLOSED;
            case LOCKED -> newStatus == ACTIVE || newStatus == SUSPENDED || newStatus == CLOSED;
            case SUSPENDED -> newStatus == ACTIVE || newStatus == BANNED || newStatus == CLOSED;
            case PENDING_CLOSURE -> newStatus == CLOSED || newStatus == ACTIVE;
            case CLOSED, BANNED -> newStatus == this; // No transitions from permanent states
        };
    }
    
    /**
     * Gets recommended next statuses that this status can transition to.
     */
    public UserStatus[] getRecommendedTransitions() {
        return switch (this) {
            case ACTIVE -> new UserStatus[]{INACTIVE, SUSPENDED, LOCKED, PENDING_CLOSURE};
            case INACTIVE -> new UserStatus[]{ACTIVE, CLOSED};
            case PENDING_VERIFICATION -> new UserStatus[]{ACTIVE, CLOSED};
            case LOCKED -> new UserStatus[]{ACTIVE, SUSPENDED};
            case SUSPENDED -> new UserStatus[]{ACTIVE, BANNED};
            case PENDING_CLOSURE -> new UserStatus[]{CLOSED, ACTIVE};
            case CLOSED, BANNED -> new UserStatus[]{}; // No transitions
        };
    }
}