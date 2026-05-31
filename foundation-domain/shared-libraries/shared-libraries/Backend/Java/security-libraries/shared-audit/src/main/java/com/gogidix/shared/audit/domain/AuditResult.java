package com.gogidix.shared.audit.domain;

/**
 * Enumeration representing the result or outcome of an audited operation.
 */
public enum AuditResult {
    SUCCESS("Operation completed successfully"),
    FAILURE("Operation failed"),
    PARTIAL_SUCCESS("Operation partially completed"),
    WARNING("Operation completed with warnings"),
    ERROR("Operation encountered errors"),
    CANCELLED("Operation was cancelled"),
    TIMEOUT("Operation timed out"),
    UNAUTHORIZED("Operation unauthorized"),
    FORBIDDEN("Operation forbidden"),
    NOT_FOUND("Resource not found"),
    CONFLICT("Operation caused conflict"),
    VALIDATION_ERROR("Validation failed"),
    BUSINESS_RULE_VIOLATION("Business rule violated"),
    SYSTEM_ERROR("System error occurred"),
    PENDING("Operation pending"),
    IN_PROGRESS("Operation in progress");
    
    private final String description;
    
    AuditResult(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Determines if this result indicates a successful operation
     */
    public boolean isSuccess() {
        return this == SUCCESS || this == PARTIAL_SUCCESS;
    }
    
    /**
     * Determines if this result indicates a failure
     */
    public boolean isFailure() {
        return this == FAILURE || 
               this == ERROR || 
               this == TIMEOUT ||
               this == SYSTEM_ERROR ||
               this == BUSINESS_RULE_VIOLATION ||
               this == VALIDATION_ERROR;
    }
    
    /**
     * Determines if this result requires immediate attention
     */
    public boolean requiresAttention() {
        return this == UNAUTHORIZED ||
               this == FORBIDDEN ||
               this == SYSTEM_ERROR ||
               this == BUSINESS_RULE_VIOLATION ||
               this == CONFLICT;
    }
    
    /**
     * Gets the severity level of this result
     */
    public AuditSeverity getSeverity() {
        switch (this) {
            case SYSTEM_ERROR:
            case UNAUTHORIZED:
            case FORBIDDEN:
                return AuditSeverity.CRITICAL;
            case FAILURE:
            case ERROR:
            case TIMEOUT:
            case BUSINESS_RULE_VIOLATION:
                return AuditSeverity.HIGH;
            case WARNING:
            case PARTIAL_SUCCESS:
            case VALIDATION_ERROR:
            case CONFLICT:
                return AuditSeverity.MEDIUM;
            case SUCCESS:
            case PENDING:
            case IN_PROGRESS:
                return AuditSeverity.LOW;
            default:
                return AuditSeverity.MEDIUM;
        }
    }
}