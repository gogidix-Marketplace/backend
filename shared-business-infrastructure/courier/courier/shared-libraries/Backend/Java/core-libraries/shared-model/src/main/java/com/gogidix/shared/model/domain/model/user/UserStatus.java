package com.gogidix.shared.model.domain.model.user;

/**
 * User Status enumeration for user lifecycle management
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B User Management
 * @version 1.0.0
 */
public enum UserStatus {
    PENDING_VERIFICATION("Pending email/phone verification"),
    ACTIVE("Active user account"),
    SUSPENDED("Temporarily suspended"),
    INACTIVE("Inactive but can be reactivated"),
    BANNED("Permanently banned"),
    PENDING_DELETION("Marked for deletion"),
    DELETED("Soft deleted account");
    
    private final String description;
    
    UserStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    public boolean canLogin() {
        return this == ACTIVE;
    }
    
    public boolean canBeReactivated() {
        return this == INACTIVE || this == SUSPENDED;
    }
    
    public boolean isPermanent() {
        return this == BANNED || this == DELETED;
    }
}