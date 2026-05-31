package com.gogidix.shared.warehousing.cyclecounting.domain.exception;

/**
 * Exception thrown when cycle count is not found
 */
public class CycleCountNotFoundException extends RuntimeException {

    public CycleCountNotFoundException(String message) {
        super(message);
    }

    public CycleCountNotFoundException(String id, String tenantId) {
        super(String.format("Cycle count not found: id=%s, tenantId=%s", id, tenantId));
    }
}
