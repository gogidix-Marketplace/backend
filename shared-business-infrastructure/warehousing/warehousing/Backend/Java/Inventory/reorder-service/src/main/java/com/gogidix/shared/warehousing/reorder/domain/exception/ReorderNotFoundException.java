package com.gogidix.shared.warehousing.reorder.domain.exception;

/**
 * Exception thrown when a reorder record is not found
 */
public class ReorderNotFoundException extends RuntimeException {

    public ReorderNotFoundException(String message) {
        super(message);
    }

    public ReorderNotFoundException(String entityType, String id) {
        super(String.format("%s not found with id: %s", entityType, id));
    }
}
