package com.gogidix.shared.model.domain.model.user;

/**
 * User Tier enumeration for feature access and business logic
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B User Management
 * @version 1.0.0
 */
public enum UserTier {
    BASIC(0, "Basic tier"),
    STANDARD(1, "Standard tier"),
    PREMIUM(2, "Premium tier"),
    VIP(3, "VIP tier"),
    ENTERPRISE(4, "Enterprise tier");
    
    private final int level;
    private final String description;
    
    UserTier(int level, String description) {
        this.level = level;
        this.description = description;
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean hasAccessTo(UserTier requiredTier) {
        return this.level >= requiredTier.level;
    }
    
    public boolean isPremium() {
        return this.level >= PREMIUM.level;
    }
    
    public boolean isEnterprise() {
        return this == ENTERPRISE;
    }
}