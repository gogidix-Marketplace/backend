package com.gogidix.shared.warehousing.batch.domain.exception;

/**
 * Exception thrown when insufficient quantity is available in a batch/lot
 */
public class InsufficientBatchQuantityException extends RuntimeException {

    public InsufficientBatchQuantityException(String lotNumber, int requested, int available) {
        super(String.format("Insufficient quantity in lot %s: requested=%d, available=%d",
            lotNumber, requested, available));
    }
}
