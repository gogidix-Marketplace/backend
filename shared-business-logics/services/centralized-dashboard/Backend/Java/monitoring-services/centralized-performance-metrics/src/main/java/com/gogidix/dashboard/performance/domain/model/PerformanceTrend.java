package com.gogidix.dashboard.performance.domain.model;

/**
 * Performance Trend Enumeration
 * 
 * Represents the trend direction of performance metrics
 */
public enum PerformanceTrend {
    IMPROVING("Performance is improving"),
    DEGRADING("Performance is degrading"), 
    STABLE("Performance is stable");
    
    private final String description;
    
    PerformanceTrend(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}