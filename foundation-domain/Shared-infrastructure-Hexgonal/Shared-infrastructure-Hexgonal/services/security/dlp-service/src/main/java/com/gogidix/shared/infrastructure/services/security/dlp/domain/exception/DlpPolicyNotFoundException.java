package com.gogidix.shared.infrastructure.services.security.dlp.domain.exception;
/**
 * Exception thrown when a DLP policy is not found.
 */
public class DlpPolicyNotFoundException extends RuntimeException {
    public DlpPolicyNotFoundException(String id) {
        super("DLP policy not found with id: " + id);
    }
}
