package com.gogidix.dashboard.performance.domain.model;

/**
 * Performance Status Enumeration
 */
public enum PerformanceStatus {
    OPTIMAL("Optimal", "Performance is within optimal range"),
    NORMAL("Normal", "Performance is acceptable"),
    WARNING("Warning", "Performance requires attention"),
    CRITICAL("Critical", "Performance is severely degraded");
    
    private final String displayName;
    private final String description;
    
    PerformanceStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
}