package com.gogidix.dashboard.core.domain.model;

/**
 * Enumeration of source domains for KPI data.
 * Defines the business domains that contribute to the centralized dashboard.
 */
public enum SourceDomain {
    COURIER_SERVICE("Courier Service", "Logistics and delivery operations"),
    WAREHOUSE("Warehouse", "Inventory and warehouse management"),
    SOCIAL_COMMERCE("Social Commerce", "Social media sales and engagement"),
    PAYMENT("Payment", "Payment processing and transactions"),
    USER_MANAGEMENT("User Management", "User accounts and authentication"),
    ANALYTICS("Analytics", "Business intelligence and reporting"),
    OPERATIONS("Operations", "General operational metrics"),
    CUSTOMER_SERVICE("Customer Service", "Support and customer satisfaction"),
    SALES("Sales", "Sales and revenue metrics"),
    MARKETING("Marketing", "Marketing campaign performance"),
    FINANCE("Finance", "Financial metrics and accounting"),
    HR("Human Resources", "HR and workforce metrics"),
    LEGACY("Legacy", "Legacy system integrations"),
    EXTERNAL("External", "External data sources"),
    AGGREGATED("Aggregated", "Cross-domain aggregated metrics");

    private final String displayName;
    private final String description;

    SourceDomain(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
