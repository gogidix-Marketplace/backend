package com.gogidix.shared.warehousing.pricing.domain.exception;

/**
 * Exception thrown when pricing request is invalid
 */
public class InvalidPricingRequestException extends RuntimeException {

    public InvalidPricingRequestException(String message) {
        super(message);
    }

    public InvalidPricingRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
