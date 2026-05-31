package com.gogidix.shared.warehousing.spaceallocation.domain.exception;

/**
 * Exception thrown when insufficient space is available
 */
public class InsufficientSpaceException extends RuntimeException {

    public InsufficientSpaceException(String message) {
        super(message);
    }

    public InsufficientSpaceException(String zoneId, Double required, Double available) {
        super(String.format("Insufficient space in zone %s: required=%.2f, available=%.2f", zoneId, required, available));
    }
}
