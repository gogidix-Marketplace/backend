package com.gogidix.ecommerce.analytics.shared.exception;

public class InvalidAnalyticsException extends RuntimeException {
    public InvalidAnalyticsException(String message) { super(message); }
    public InvalidAnalyticsException(String message, Throwable cause) { super(message, cause); }
}
