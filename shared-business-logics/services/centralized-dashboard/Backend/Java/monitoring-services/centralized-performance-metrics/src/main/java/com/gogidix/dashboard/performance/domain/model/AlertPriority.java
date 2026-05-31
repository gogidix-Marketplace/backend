package com.gogidix.dashboard.performance.domain.model;

/**
 * Alert Priority Enumeration
 * 
 * Represents the priority level of performance alerts
 */
public enum AlertPriority {
    P1("Critical - Immediate response required"),
    P2("High - Response required within 30 minutes"),
    P3("Medium - Response required within 2 hours"),
    P4("Low - Response required within 8 hours");
    
    private final String description;
    
    AlertPriority(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean requiresImmediateResponse() {
        return this == P1;
    }
}