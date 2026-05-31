package com.gogidix.shared.infrastructure.services.security.dlp.domain.exception;
/**
 * Exception thrown when DLP policy validation fails.
 */
public class DlpPolicyValidationException extends RuntimeException {
    public DlpPolicyValidationException(String message) {
        super(message);
    }
}
