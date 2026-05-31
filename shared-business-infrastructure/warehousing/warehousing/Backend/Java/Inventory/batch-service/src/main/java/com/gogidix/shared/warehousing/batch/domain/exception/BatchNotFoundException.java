package com.gogidix.shared.warehousing.batch.domain.exception;

/**
 * Exception thrown when a batch is not found
 */
public class BatchNotFoundException extends RuntimeException {

    public BatchNotFoundException(String message) {
        super(message);
    }

    public BatchNotFoundException(String batchType, String id) {
        super(String.format("%s not found with id: %s", batchType, id));
    }
}
