package com.gogidix.shared.exceptions.domain.exception;

/**
 * Technical/infrastructure exceptions (5xx errors)
 * Thrown when technical issues occur
 */
public class TechnicalException extends BaseException {

    public TechnicalException(String message) {
        super(message, "TECHNICAL_ERROR", 500, "TECHNICAL");
    }

    public TechnicalException(String message, String errorCode) {
        super(message, errorCode, 500, "TECHNICAL");
    }

    public TechnicalException(String message, Throwable cause) {
        super(message, cause, "TECHNICAL_ERROR", 500, "TECHNICAL");
    }

    public TechnicalException(String message, String errorCode, int httpStatus) {
        super(message, errorCode, httpStatus, "TECHNICAL");
    }

    public TechnicalException(String message, Throwable cause, String errorCode, int httpStatus) {
        super(message, cause, errorCode, httpStatus, "TECHNICAL");
    }

    // Constructor with custom category for subclasses like DatabaseException
    protected TechnicalException(String message, String errorCode, int httpStatus, String category) {
        super(message, errorCode, httpStatus, category);
    }

    // Constructor with custom category for subclasses (with cause)
    protected TechnicalException(String message, Throwable cause, String errorCode, int httpStatus, String category) {
        super(message, cause, errorCode, httpStatus, category);
    }

    @Override
    public boolean isRetryable() {
        return false; // Technical errors are not automatically retryable (need manual intervention)
    }
}
