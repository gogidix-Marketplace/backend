package com.gogidix.shared.audit.domain;

/**
 * Enumeration of compliance frameworks and regulations applicable to the Gogidix ecosystem.
 */
public enum ComplianceType {
    
    PCI_DSS("PCI DSS", "Payment Card Industry Data Security Standard", true),
    GDPR("GDPR", "General Data Protection Regulation", true),
    SOX("SOX", "Sarbanes-Oxley Act", true),
    HIPAA("HIPAA", "Health Insurance Portability and Accountability Act", false),
    CCPA("CCPA", "California Consumer Privacy Act", false),
    ISO_27001("ISO 27001", "Information Security Management", false),
    SOC_2("SOC 2", "Service Organization Control 2", false),
    INDUSTRY_SPECIFIC("Industry Specific", "Industry-specific compliance requirements", false),
    INTERNAL_POLICY("Internal Policy", "Internal governance and policy compliance", true),
    DATA_PROTECTION("Data Protection", "General data protection compliance", true);
    
    private final String code;
    private final String fullName;
    private final boolean mandatory;
    
    ComplianceType(String code, String fullName, boolean mandatory) {
        this.code = code;
        this.fullName = fullName;
        this.mandatory = mandatory;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public boolean isMandatory() {
        return mandatory;
    }
    
    /**
     * Determines if this compliance type requires real-time monitoring
     */
    public boolean requiresRealTimeMonitoring() {
        return this == PCI_DSS || 
               this == SOX || 
               this == HIPAA;
    }
    
    /**
     * Gets the maximum allowed audit log retention period for this compliance type
     */
    public int getMaxRetentionDays() {
        switch (this) {
            case PCI_DSS:
                return 365; // 1 year minimum
            case SOX:
                return 2555; // 7 years
            case GDPR:
                return 1095; // 3 years unless data subject requests deletion
            case HIPAA:
                return 2190; // 6 years
            case CCPA:
                return 730; // 2 years
            default:
                return 1095; // 3 years default
        }
    }
    
    /**
     * Gets the minimum audit detail level required for this compliance type
     */
    public AuditDetailLevel getMinimumDetailLevel() {
        switch (this) {
            case PCI_DSS:
            case SOX:
            case HIPAA:
                return AuditDetailLevel.COMPREHENSIVE;
            case GDPR:
            case CCPA:
                return AuditDetailLevel.DETAILED;
            default:
                return AuditDetailLevel.STANDARD;
        }
    }
    
    /**
     * Determines if this compliance type requires encryption of audit logs
     */
    public boolean requiresLogEncryption() {
        return this == PCI_DSS ||
               this == HIPAA ||
               this == SOX;
    }
    
    /**
     * Gets the required audit trail completeness for this compliance type
     */
    public double getRequiredTrailCompleteness() {
        switch (this) {
            case PCI_DSS:
            case SOX:
            case HIPAA:
                return 1.0; // 100% completeness required
            case GDPR:
            case CCPA:
                return 0.95; // 95% completeness required
            default:
                return 0.90; // 90% completeness required
        }
    }
}