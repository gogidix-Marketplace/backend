package com.gogidix.shared.audit.domain;

/**
 * Enumeration of all audit event types in the Gogidix ecosystem.
 * Each type represents a category of actions that require audit tracking.
 */
public enum AuditEventType {
    
    // User Management Events
    USER_LOGIN("User authentication login"),
    USER_LOGOUT("User logout"),
    LOGIN_ATTEMPT("Failed login attempt"),
    PASSWORD_CHANGE("Password modification"),
    USER_REGISTRATION("New user registration"),
    USER_PROFILE_UPDATE("User profile modification"),
    ACCOUNT_LOCKOUT("Account security lockout"),
    ACCOUNT_UNLOCK("Account unlock operation"),
    
    // Financial Transaction Events
    FINANCIAL_TRANSACTION("Financial transaction processing"),
    PAYMENT_PROCESSING("Payment gateway interaction"),
    REFUND_PROCESSING("Refund transaction"),
    REFUND_PROCESSED("Refund completed"),
    COMMISSION_CALCULATION("Commission calculation"),
    INVOICE_GENERATION("Invoice creation"),
    PAYMENT_FAILURE("Payment processing failure"),
    CHARGEBACK_EVENT("Chargeback processing"),
    SETTLEMENT_PROCESSING("Payment settlement"),
    
    // Data Access Events
    DATA_ACCESS("Data record access"),
    DATA_MODIFICATION("Data record modification"),
    DATA_DELETION("Data record deletion"),
    DATA_EXPORT("Data export operation"),
    DATA_IMPORT("Data import operation"),
    BULK_DATA_OPERATION("Bulk data processing"),
    SENSITIVE_DATA_ACCESS("Access to sensitive information"),
    
    // Security Events
    SECURITY_EVENT("Security-related event"),
    ACCESS_DENIED("Access denied event"),
    PRIVILEGE_ESCALATION("Privilege escalation attempt"),
    UNAUTHORIZED_ACCESS("Unauthorized access attempt"),
    SECURITY_POLICY_VIOLATION("Security policy violation"),
    MALICIOUS_ACTIVITY("Detected malicious activity"),
    VULNERABILITY_DETECTED("Security vulnerability detected"),
    
    // System Events
    SYSTEM_EVENT("System operation"),
    SERVICE_START("Service startup"),
    SERVICE_STOP("Service shutdown"),
    CONFIGURATION_CHANGE("System configuration change"),
    BACKUP_OPERATION("Backup operation"),
    RESTORE_OPERATION("Restore operation"),
    MAINTENANCE_EVENT("System maintenance"),
    PERFORMANCE_ALERT("Performance threshold alert"),
    
    // Business Process Events
    ORDER_PLACED("Customer order placement"),
    ORDER_FULFILLED("Order fulfillment completion"),
    ORDER_CANCELLED("Order cancellation"),
    INVENTORY_UPDATE("Inventory level change"),
    PRICE_CHANGE("Product price modification"),
    PROMOTION_APPLIED("Promotional discount applied"),
    SHIPPING_EVENT("Shipping status update"),
    DELIVERY_CONFIRMATION("Delivery confirmation"),
    
    // Compliance Events
    COMPLIANCE_CHECK("Compliance verification"),
    REGULATORY_REPORT("Regulatory reporting"),
    GDPR_REQUEST("GDPR data request"),
    PCI_COMPLIANCE_CHECK("PCI DSS compliance check"),
    SOX_COMPLIANCE_EVENT("SOX compliance event"),
    AUDIT_TRAIL_ACCESS("Audit trail access"),
    
    // Communication Events
    EMAIL_SENT("Email communication"),
    SMS_SENT("SMS communication"),
    NOTIFICATION_SENT("System notification"),
    WEBHOOK_TRIGGERED("Webhook event trigger"),
    API_CALL("External API call"),
    
    // Workflow Events
    WORKFLOW_STARTED("Business workflow initiation"),
    WORKFLOW_COMPLETED("Business workflow completion"),
    WORKFLOW_FAILED("Business workflow failure"),
    APPROVAL_REQUESTED("Approval request"),
    APPROVAL_GRANTED("Approval granted"),
    APPROVAL_DENIED("Approval denied"),
    
    // Integration Events
    THIRD_PARTY_INTEGRATION("Third-party service integration"),
    API_KEY_USAGE("API key authentication"),
    WEBHOOK_RECEIVED("Incoming webhook"),
    FILE_UPLOAD("File upload operation"),
    FILE_DOWNLOAD("File download operation"),
    
    // Error Events
    APPLICATION_ERROR("Application error"),
    VALIDATION_ERROR("Data validation error"),
    BUSINESS_RULE_VIOLATION("Business rule violation"),
    EXCEPTION_THROWN("Exception occurrence"),
    
    // Administrative Events
    ADMIN_ACTION("Administrative action"),
    USER_ROLE_CHANGE("User role modification"),
    PERMISSION_GRANT("Permission granted"),
    PERMISSION_REVOKE("Permission revoked"),
    SYSTEM_CONFIGURATION("System configuration"),
    FEATURE_FLAG_CHANGE("Feature flag modification");
    
    private final String description;
    
    AuditEventType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Determines if this event type requires enhanced security monitoring
     */
    public boolean requiresSecurityMonitoring() {
        return this == SECURITY_EVENT ||
               this == ACCESS_DENIED ||
               this == PRIVILEGE_ESCALATION ||
               this == UNAUTHORIZED_ACCESS ||
               this == MALICIOUS_ACTIVITY ||
               this == SECURITY_POLICY_VIOLATION;
    }
    
    /**
     * Determines if this event type is financial and requires compliance tracking
     */
    public boolean isFinancialEvent() {
        return this == FINANCIAL_TRANSACTION ||
               this == PAYMENT_PROCESSING ||
               this == REFUND_PROCESSING ||
               this == REFUND_PROCESSED ||
               this == COMMISSION_CALCULATION ||
               this == INVOICE_GENERATION ||
               this == CHARGEBACK_EVENT ||
               this == SETTLEMENT_PROCESSING;
    }
    
    /**
     * Determines if this event type involves sensitive data access
     */
    public boolean involvesSensitiveData() {
        return this == SENSITIVE_DATA_ACCESS ||
               this == DATA_EXPORT ||
               this == GDPR_REQUEST ||
               this == FINANCIAL_TRANSACTION ||
               this == USER_PROFILE_UPDATE;
    }
    
    /**
     * Gets the default retention period for this event type in days
     */
    public int getDefaultRetentionDays() {
        if (isFinancialEvent()) {
            return 2555; // 7 years for financial records
        }
        if (requiresSecurityMonitoring()) {
            return 1095; // 3 years for security events
        }
        if (involvesSensitiveData()) {
            return 365; // 1 year for sensitive data
        }
        return 90; // 90 days for general events
    }
}