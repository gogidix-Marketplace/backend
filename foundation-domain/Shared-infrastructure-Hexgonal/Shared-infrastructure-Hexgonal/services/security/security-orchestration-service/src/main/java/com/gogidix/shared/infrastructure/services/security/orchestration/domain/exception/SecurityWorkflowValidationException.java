package com.gogidix.shared.infrastructure.services.security.orchestration.domain.exception;
/**
 * Exception thrown when security workflow validation fails.
 */
public class SecurityWorkflowValidationException extends RuntimeException {
    public SecurityWorkflowValidationException(String message) {
        super(message);
    }
}
