package com.gogidix.aiservices.aifrauddetectionservice.shared.exception;

/**
 * Error codes for fraud detection exceptions.
 */
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("INTERNAL_ERROR"),
    POLICY_VIOLATION("POLICY_VIOLATION"),
    FRAUD_THRESHOLD_EXCEEDED("FRAUD_THRESHOLD_EXCEEDED"),
    SUSPICIOUS_PATTERN_DETECTED("SUSPICIOUS_PATTERN"),
    INVALID_REQUEST("INVALID_REQUEST"),
    RESOURCE_NOT_FOUND("NOT_FOUND"),
    UNAUTHORIZED_ACCESS("UNAUTHORIZED"),
    VALIDATION_ERROR("VALIDATION_ERROR"),
    TRANSACTION_ALREADY_PROCESSED("TXN_ALREADY_PROCESSED"),
    PATTERN_ALREADY_EXISTS("PATTERN_EXISTS"),
    PATTERN_NOT_FOUND("PATTERN_NOT_FOUND"),
    ANALYSIS_NOT_FOUND("ANALYSIS_NOT_FOUND"),
    COMPLIANCE_CHECK_FAILED("COMPLIANCE_FAILED");
    
    private final String code;
    
    ErrorCode(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
}
