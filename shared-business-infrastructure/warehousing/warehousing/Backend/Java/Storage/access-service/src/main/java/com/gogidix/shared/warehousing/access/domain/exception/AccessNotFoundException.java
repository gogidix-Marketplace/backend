package com.gogidix.shared.warehousing.access.domain.exception;

/**
 * Exception thrown when access resource is not found
 */
public class AccessNotFoundException extends RuntimeException {

    public AccessNotFoundException(String message) {
        super(message);
    }

    public AccessNotFoundException(String id, String tenantId) {
        super(String.format("Access resource not found: id=%s, tenantId=%s", id, tenantId));
    }
}
