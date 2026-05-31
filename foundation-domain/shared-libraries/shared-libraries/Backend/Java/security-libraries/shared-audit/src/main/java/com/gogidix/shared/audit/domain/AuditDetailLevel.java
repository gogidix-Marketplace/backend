package com.gogidix.shared.audit.domain;

/**
 * Enumeration representing the level of detail captured in audit events.
 */
public enum AuditDetailLevel {
    
    MINIMAL("Minimal", "Basic event information only"),
    STANDARD("Standard", "Standard audit information"),
    DETAILED("Detailed", "Detailed audit information with context"),
    COMPREHENSIVE("Comprehensive", "Complete audit trail with full context"),
    FORENSIC("Forensic", "Maximum detail for forensic analysis");
    
    private final String displayName;
    private final String description;
    
    AuditDetailLevel(String displayName, String description) {
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
     * Determines if this detail level includes metadata capture
     */
    public boolean includesMetadata() {
        return this == DETAILED || this == COMPREHENSIVE || this == FORENSIC;
    }
    
    /**
     * Determines if this detail level includes performance metrics
     */
    public boolean includesPerformanceMetrics() {
        return this == COMPREHENSIVE || this == FORENSIC;
    }
    
    /**
     * Determines if this detail level includes full request/response data
     */
    public boolean includesFullRequestResponse() {
        return this == FORENSIC;
    }
}