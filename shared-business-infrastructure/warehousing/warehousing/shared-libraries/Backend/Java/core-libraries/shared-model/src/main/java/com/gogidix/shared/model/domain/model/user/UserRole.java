package com.gogidix.shared.model.domain.model.user;

/**
 * User Role enumeration for authorization
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B User Management
 * @version 1.0.0
 */
public enum UserRole {
    // Basic roles
    USER("Basic user"),
    PREMIUM_USER("Premium subscriber"),
    VENDOR("Vendor/seller"),
    
    // Administrative roles
    MODERATOR("Content moderator"),
    ADMIN("System administrator"),
    SUPER_ADMIN("Super administrator"),
    
    // Organizational roles
    MANAGER("Department manager"),
    EMPLOYEE("Organization employee"),
    CONTRACTOR("External contractor"),
    
    // Special roles
    BETA_TESTER("Beta feature tester"),
    SUPPORT_AGENT("Customer support"),
    AUDITOR("System auditor"),
    DEVELOPER("Platform developer");
    
    private final String description;
    
    UserRole(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isAdministrative() {
        return this == ADMIN || this == SUPER_ADMIN || this == MODERATOR;
    }
    
    public boolean isOrganizational() {
        return this == MANAGER || this == EMPLOYEE || this == CONTRACTOR;
    }
    
    public boolean hasElevatedPrivileges() {
        return isAdministrative() || this == SUPPORT_AGENT || this == AUDITOR;
    }
}