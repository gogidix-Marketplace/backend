package com.gogidix.shared.warehousing.access.domain.exception;

/**
 * Exception thrown when access is denied
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String userId, String warehouseId, String reason) {
        super(String.format("Access denied for user %s to warehouse %s: %s", userId, warehouseId, reason));
    }
}
