package com.gogidix.shared.audit.domain;

/**
 * Enumeration representing the risk level assessment of audit events.
 * Used to classify events based on their potential business/operational risk.
 */
public enum RiskLevel {
    
    CRITICAL(4, "Critical", "Critical risk - potential system/business failure"),
    HIGH(3, "High", "High risk - significant impact possible"),
    MEDIUM(2, "Medium", "Medium risk - moderate impact possible"),
    LOW(1, "Low", "Low risk - minimal impact"),
    NEGLIGIBLE(0, "Negligible", "Negligible risk - no significant impact");
    
    private final int level;
    private final String displayName;
    private final String description;
    
    RiskLevel(int level, String displayName, String description) {
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
     * Get base score for severity calculation (0-40 scale)
     */
    public int getBaseScore() {
        switch (this) {
            case CRITICAL:
                return 40;
            case HIGH:
                return 30;
            case MEDIUM:
                return 20;
            case LOW:
                return 10;
            default:
                return 0;
        }
    }
    
    /**
     * Determines if this risk level requires immediate risk management action
     */
    public boolean requiresImmediateAction() {
        return this == CRITICAL || this == HIGH;
    }
    
    /**
     * Determines if this risk level requires risk committee notification
     */
    public boolean requiresCommitteeNotification() {
        return this.level >= MEDIUM.level;
    }
    
    /**
     * Determines if this risk level requires detailed audit trail
     */
    public boolean requiresDetailedAuditTrail() {
        return this == CRITICAL || this == HIGH;
    }
    
    /**
     * Gets the review period in hours for this risk level
     */
    public int getReviewPeriodHours() {
        switch (this) {
            case CRITICAL:
                return 1;     // 1 hour
            case HIGH:
                return 4;     // 4 hours
            case MEDIUM:
                return 24;    // 24 hours
            case LOW:
                return 168;   // 1 week
            default:
                return 0;     // No review required
        }
    }
    
    /**
     * Determines if financial approval is required for this risk level
     */
    public boolean requiresFinancialApproval() {
        return this == CRITICAL || this == HIGH;
    }
}
