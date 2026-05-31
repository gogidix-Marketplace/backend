package com.gogidix.shared.infrastructure.services.security.analytics.domain.exception;
/**
 * Exception thrown when a security event is not found.
 */
public class SecurityEventNotFoundException extends RuntimeException {
    public SecurityEventNotFoundException(String id) {
        super("Security event not found with id: " + id);
    }
}
