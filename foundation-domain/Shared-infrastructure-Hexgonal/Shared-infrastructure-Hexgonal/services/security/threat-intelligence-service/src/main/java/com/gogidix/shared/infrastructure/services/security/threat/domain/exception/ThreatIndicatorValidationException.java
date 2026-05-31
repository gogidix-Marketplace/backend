package com.gogidix.shared.infrastructure.services.security.threat.domain.exception;
/**
 * Exception thrown when threat indicator validation fails.
 */
public class ThreatIndicatorValidationException extends RuntimeException {
    public ThreatIndicatorValidationException(String message) {
        super(message);
    }
}
