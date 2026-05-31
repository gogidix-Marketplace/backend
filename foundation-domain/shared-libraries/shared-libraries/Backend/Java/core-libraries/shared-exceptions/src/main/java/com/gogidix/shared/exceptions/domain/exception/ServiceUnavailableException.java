package com.gogidix.shared.exceptions.domain.exception;

/**
 * Service unavailable exception (503)
 */
public class ServiceUnavailableException extends TechnicalException {
    
    public ServiceUnavailableException(String message) {
        super(message, "SERVICE_UNAVAILABLE", 503);
    }
    
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause, "SERVICE_UNAVAILABLE", 503);
    }
    
    @Override
    public boolean isRetryable() {
        return true;
    }
}
