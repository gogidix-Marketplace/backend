package com.gogidix.shared.infrastructure.services.security.analytics.domain.exception;
/**
 * Exception thrown when security event validation fails.
 */
public class SecurityEventValidationException extends RuntimeException {
    public SecurityEventValidationException(String message) {
        super(message);
    }
}
