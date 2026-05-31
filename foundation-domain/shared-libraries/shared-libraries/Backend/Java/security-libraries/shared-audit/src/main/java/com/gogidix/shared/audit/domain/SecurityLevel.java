package com.gogidix.shared.audit.domain;

/**
 * Enumeration representing the security level classification of audit events.
 * Used to classify events based on their security implications.
 */
public enum SecurityLevel {
    
    CRITICAL(4, "Critical", "Highest security priority - immediate action required"),
    HIGH(3, "High", "High security concern - action required within 1 hour"),
    ELEVATED(2, "Elevated", "Elevated security state - monitor closely"),
    NORMAL(1, "Normal", "Standard security operations"),
    LOW(0, "Low", "Low security significance");
    
    private final int level;
    private final String displayName;
    private final String description;
    
    SecurityLevel(int level, String displayName, String description) {
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
     * Determines if this security level requires immediate security team notification
     */
    public boolean requiresSecurityTeamAlert() {
        return this == CRITICAL || this == HIGH;
    }
    
    /**
     * Determines if this security level requires enhanced monitoring
     */
    public boolean requiresEnhancedMonitoring() {
        return this.level >= ELEVATED.level;
    }
    
    /**
     * Determines if this security level requires access log encryption
     */
    public boolean requiresEncryption() {
        return this == CRITICAL || this == HIGH;
    }
    
    /**
     * Gets the escalation timeout in minutes for this security level
     */
    public int getEscalationTimeoutMinutes() {
        switch (this) {
            case CRITICAL:
                return 5;     // 5 minutes
            case HIGH:
                return 30;    // 30 minutes
            case ELEVATED:
                return 120;   // 2 hours
            default:
                return 0;     // No escalation
        }
    }
}
