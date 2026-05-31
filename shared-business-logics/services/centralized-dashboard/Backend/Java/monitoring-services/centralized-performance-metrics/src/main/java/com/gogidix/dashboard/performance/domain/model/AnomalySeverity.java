package com.gogidix.dashboard.performance.domain.model;

/**
 * Anomaly Severity Enumeration
 * 
 * Represents the severity level of performance anomalies
 */
public enum AnomalySeverity {
    P0("Critical - Immediate action required"),
    P1("High - Action required within 1 hour"),
    P2("Medium - Action required within 4 hours"), 
    P3("Low - Action required within 24 hours"),
    P4("Info - No immediate action required");
    
    private final String description;
    
    AnomalySeverity(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isCritical() {
        return this == P0 || this == P1;
    }
}