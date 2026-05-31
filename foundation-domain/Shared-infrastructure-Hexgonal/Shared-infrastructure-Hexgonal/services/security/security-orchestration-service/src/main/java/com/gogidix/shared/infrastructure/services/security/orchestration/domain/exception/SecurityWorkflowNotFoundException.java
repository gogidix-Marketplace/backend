package com.gogidix.shared.infrastructure.services.security.orchestration.domain.exception;
/**
 * Exception thrown when a security workflow is not found.
 */
public class SecurityWorkflowNotFoundException extends RuntimeException {
    public SecurityWorkflowNotFoundException(String id) {
        super("Security workflow not found with id: " + id);
    }
}
