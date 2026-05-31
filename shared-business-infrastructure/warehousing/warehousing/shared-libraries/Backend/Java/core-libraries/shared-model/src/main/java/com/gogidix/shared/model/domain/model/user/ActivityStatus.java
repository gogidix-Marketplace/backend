package com.gogidix.shared.model.domain.model.user;

/**
 * User Activity Status for behavioral analysis
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B User Management
 * @version 1.0.0
 */
public enum ActivityStatus {
    VERY_ACTIVE("Very active user"),
    ACTIVE("Active user"),
    MODERATE("Moderately active"),
    INACTIVE("Inactive user"),
    DORMANT("Dormant account");
    
    private final String description;
    
    ActivityStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isEngaged() {
        return this == VERY_ACTIVE || this == ACTIVE;
    }
    
    public boolean needsReengagement() {
        return this == INACTIVE || this == DORMANT;
    }
}