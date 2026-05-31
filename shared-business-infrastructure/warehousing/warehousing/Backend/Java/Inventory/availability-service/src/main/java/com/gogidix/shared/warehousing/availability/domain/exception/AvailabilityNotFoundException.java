package com.gogidix.shared.warehousing.availability.domain.exception;

/**
 * Exception thrown when availability resource is not found
 */
public class AvailabilityNotFoundException extends RuntimeException {

    public AvailabilityNotFoundException(String message) {
        super(message);
    }

    public AvailabilityNotFoundException(String id, String tenantId) {
        super(String.format("Availability resource not found: id=%s, tenantId=%s", id, tenantId));
    }
}
