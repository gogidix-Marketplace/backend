package com.gogidix.shared.infrastructure.services.security.threat.domain.exception;
/**
 * Exception thrown when a threat indicator is not found.
 */
public class ThreatIndicatorNotFoundException extends RuntimeException {
    public ThreatIndicatorNotFoundException(String id) {
        super("Threat indicator not found with id: " + id);
    }
}
