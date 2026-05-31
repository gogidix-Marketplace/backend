package com.gogidix.shared.model.domain.model.user;

/**
 * User Risk Level for security assessment
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B User Management
 * @version 1.0.0
 */
public enum UserRiskLevel {
    MINIMAL(0, "Minimal risk"),
    LOW(1, "Low risk"),
    MEDIUM(2, "Medium risk"),
    HIGH(3, "High risk"),
    CRITICAL(4, "Critical risk");
    
    private final int level;
    private final String description;
    
    UserRiskLevel(int level, String description) {
        this.level = level;
        this.description = description;
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean requiresAdditionalSecurity() {
        return this.level >= MEDIUM.level;
    }
    
    public boolean requiresManualReview() {
        return this.level >= HIGH.level;
    }
}