package com.gogidix.shared.audit.domain;

/**
 * Enumeration representing security threat levels.
 */
public enum SecurityThreatLevel {
    
    MINIMAL("Minimal", "No significant threats detected"),
    LOW("Low", "Minor security concerns"),
    MODERATE("Moderate", "Some security concerns requiring attention"),
    HIGH("High", "Significant security threats detected"),
    CRITICAL("Critical", "Immediate security response required");
    
    private final String displayName;
    private final String description;
    
    SecurityThreatLevel(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Determines if this threat level requires immediate escalation
     */
    public boolean requiresImmediateEscalation() {
        return this == CRITICAL || this == HIGH;
    }
    
    /**
     * Gets the maximum response time in minutes for this threat level
     */
    public int getMaxResponseTimeMinutes() {
        switch (this) {
            case CRITICAL:
                return 5;
            case HIGH:
                return 30;
            case MODERATE:
                return 240; // 4 hours
            case LOW:
                return 1440; // 24 hours
            default:
                return 0; // No specific response time
        }
    }
}