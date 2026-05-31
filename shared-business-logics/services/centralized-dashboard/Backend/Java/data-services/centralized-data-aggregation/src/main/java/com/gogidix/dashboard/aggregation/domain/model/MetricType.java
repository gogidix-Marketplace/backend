package com.gogidix.dashboard.aggregation.domain.model;

/**
 * Metric Type Enumeration
 * 
 * Defines the different types of metrics that can be aggregated
 */
public enum MetricType {
    BUSINESS_KPI("Business KPI", true, true),
    OPERATIONAL("Operational", false, true), 
    TECHNICAL("Technical", false, true),
    FINANCIAL("Financial", true, true),
    CUSTOMER("Customer", true, true),
    PERFORMANCE("Performance", false, true),
    QUALITY("Quality", false, true),
    SECURITY("Security", false, true);
    
    private final String displayName;
    private final boolean businessCritical;
    private final boolean supportsAlerting;
    
    MetricType(String displayName, boolean businessCritical, boolean supportsAlerting) {
        this.displayName = displayName;
        this.businessCritical = businessCritical;
        this.supportsAlerting = supportsAlerting;
    }
    
    public String getDisplayName() { return displayName; }
    public boolean isBusinessCritical() { return businessCritical; }
    public boolean isOperational() { return this == OPERATIONAL || this == TECHNICAL || this == PERFORMANCE; }
    public boolean supportsAlerting() { return supportsAlerting; }
}