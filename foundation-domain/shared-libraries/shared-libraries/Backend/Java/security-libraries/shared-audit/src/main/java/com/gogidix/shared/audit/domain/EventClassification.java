package com.gogidix.shared.audit.domain;

/**
 * Enumeration representing the business classification of audit events.
 * Used for event categorization and reporting purposes.
 */
public enum EventClassification {
    
    SYSTEM_CRITICAL("System Critical", "Events that impact system availability or integrity"),
    HIGH_RISK("High Risk", "Events with high business or operational risk"),
    SECURITY("Security", "Security-related events"),
    FINANCIAL("Financial", "Financial transaction events"),
    COMPLIANCE("Compliance", "Compliance and regulatory events"),
    STANDARD("Standard", "Standard operational events");
    
    private final String displayName;
    private final String description;
    
    EventClassification(String displayName, String description) {
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
     * Determines if this classification requires executive reporting
     */
    public boolean requiresExecutiveReporting() {
        return this == SYSTEM_CRITICAL || this == HIGH_RISK;
    }
    
    /**
     * Determines if this classification requires regulatory reporting
     */
    public boolean requiresRegulatoryReporting() {
        return this == COMPLIANCE || this == FINANCIAL;
    }
}
