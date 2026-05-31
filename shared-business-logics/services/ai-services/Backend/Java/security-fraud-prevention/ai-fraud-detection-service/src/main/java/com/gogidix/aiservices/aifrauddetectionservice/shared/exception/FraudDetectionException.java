package com.gogidix.aiservices.aifrauddetectionservice.shared.exception;

import lombok.Getter;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.UUID;

/**
 * Base exception class for all fraud detection related exceptions.
 * <p>
 * This exception includes an error code, timestamp, and unique error ID
 * for tracking and debugging purposes.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class FraudDetectionException extends RuntimeException {

    private final String errorId;
    private final ErrorCode errorCode;
    private final Instant timestamp;

    /**
     * Constructs a new FraudDetectionException with the specified error code and message.
     *
     * @param errorCode the error code enum
     * @param message   the detail message
     */
    public FraudDetectionException(ErrorCode errorCode, String message) {
        super(message);
        this.errorId = UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
    }

    /**
     * Constructs a new FraudDetectionException with error code, message, and cause.
     *
     * @param errorCode the error code enum
     * @param message   the detail message
     * @param cause     the root cause
     */
    public FraudDetectionException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorId = UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
    }

    /**
     * Constructs a new FraudDetectionException with all fields.
     *
     * @param errorCode the error code enum
     * @param message   the detail message
     * @param cause     the root cause
     * @param errorId   the unique error ID
     * @param timestamp the timestamp when the error occurred
     */
    public FraudDetectionException(ErrorCode errorCode, String message, Throwable cause, String errorId, Instant timestamp) {
        super(message, cause);
        this.errorId = errorId;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    /**
     * Creates a FraudDetectionException with INTERNAL_SERVER_ERROR code.
     *
     * @param message the detail message
     * @return a new FraudDetectionException instance
     */
    public static FraudDetectionException internalError(String message) {
        return new FraudDetectionException(ErrorCode.INTERNAL_SERVER_ERROR, message);
    }

    /**
     * Creates a FraudDetectionException with INTERNAL_SERVER_ERROR code and cause.
     *
     * @param message the detail message
     * @param cause   the root cause
     * @return a new FraudDetectionException instance
     */
    public static FraudDetectionException internalError(String message, Throwable cause) {
        return new FraudDetectionException(ErrorCode.INTERNAL_SERVER_ERROR, message, cause);
    }
}
