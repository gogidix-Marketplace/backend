package com.gogidix.shared.audit.domain;

/**
 * Enumeration of business domains in the Gogidix ecosystem.
 * Each domain represents a major business area that generates audit events.
 */
public enum BusinessDomain {

    SHARED_INFRASTRUCTURE("shared-infrastructure", "Core infrastructure services"),
    SHARED_LIBRARIES("shared-libraries", "Shared library components"),
    CENTRAL_CONFIGURATION("central-configuration", "Configuration management"),
    CENTRALIZED_DASHBOARD("centralized-dashboard", "Executive dashboards and analytics"),

    MANAGEMENT_SUPPORT("management-support", "Management and administrative support"),
    SOCIAL_COMMERCE("social-commerce", "Social commerce marketplace"),
    WAREHOUSING("warehousing", "Warehouse and inventory management"),
    COURIER_SERVICES("courier-services", "Courier and delivery services"),
    HAULAGE_LOGISTICS("haulage-logistics", "Heavy haulage and logistics"),
    AI_SERVICES("ai-services", "Artificial intelligence services"),
    CORPORATE_WEBSITE("corporate-website", "Corporate website and CMS"),

    // Additional domains for testing
    IDENTITY("identity", "Identity and access management"),
    PAYMENTS("payments", "Payment processing"),
    SECURITY("security", "Security operations"),
    COMMISSIONS("commissions", "Commission processing"),
    BILLING("billing", "Billing and invoicing"),
    ORDERS("orders", "Order management");
    
    private final String domainCode;
    private final String description;
    
    BusinessDomain(String domainCode, String description) {
        this.domainCode = domainCode;
        this.description = description;
    }
    
    public String getDomainCode() {
        return domainCode;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Determines if this domain handles financial transactions
     */
    public boolean isFinancialDomain() {
        return this == SOCIAL_COMMERCE || 
               this == MANAGEMENT_SUPPORT ||
               this == COURIER_SERVICES ||
               this == HAULAGE_LOGISTICS;
    }
    
    /**
     * Determines if this domain requires enhanced security monitoring
     */
    public boolean requiresEnhancedSecurity() {
        return this == SOCIAL_COMMERCE ||
               this == MANAGEMENT_SUPPORT ||
               this == SHARED_INFRASTRUCTURE ||
               this == CENTRAL_CONFIGURATION;
    }
    
    /**
     * Gets the compliance requirements for this domain
     */
    public ComplianceType[] getComplianceRequirements() {
        switch (this) {
            case SOCIAL_COMMERCE:
                return new ComplianceType[]{ComplianceType.PCI_DSS, ComplianceType.GDPR};
            case MANAGEMENT_SUPPORT:
                return new ComplianceType[]{ComplianceType.SOX, ComplianceType.GDPR};
            case WAREHOUSING:
            case COURIER_SERVICES:
            case HAULAGE_LOGISTICS:
                return new ComplianceType[]{ComplianceType.GDPR, ComplianceType.INDUSTRY_SPECIFIC};
            default:
                return new ComplianceType[]{ComplianceType.GDPR};
        }
    }
    
    /**
     * Gets the default audit retention period for this domain
     */
    public int getDefaultRetentionDays() {
        if (isFinancialDomain()) {
            return 2555; // 7 years
        }
        return 1095; // 3 years
    }
}