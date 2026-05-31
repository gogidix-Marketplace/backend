package com.gogidix.shared.audit.domain;

/**
 * Enumeration representing the severity level of audit events.
 */
public enum AuditSeverity {
    
    CRITICAL(4, "Critical", "Immediate action required"),
    HIGH(3, "High", "Action required within 1 hour"),
    MEDIUM(2, "Medium", "Action required within 24 hours"),
    LOW(1, "Low", "Monitor and review"),
    INFO(0, "Informational", "For information only");
    
    private final int level;
    private final String displayName;
    private final String description;
    
    AuditSeverity(int level, String displayName, String description) {
        this.level = level;
        this.displayName = displayName;
        this.description = description;
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Determines if this severity requires immediate notification
     */
    public boolean requiresImmediateNotification() {
        return this == CRITICAL || this == HIGH;
    }
    
    /**
     * Gets the escalation timeout in minutes for this severity level
     */
    public int getEscalationTimeoutMinutes() {
        switch (this) {
            case CRITICAL:
                return 5; // 5 minutes
            case HIGH:
                return 60; // 1 hour
            case MEDIUM:
                return 1440; // 24 hours
            default:
                return 0; // No escalation
        }
    }
    
    /**
     * Determines if this severity level requires audit log encryption
     */
    public boolean requiresEncryption() {
        return this == CRITICAL || this == HIGH;
    }
}