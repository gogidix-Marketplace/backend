package com.gogidix.ecommerce.notification.shared.exception;

public class InvalidNotificationException extends RuntimeException {
    public InvalidNotificationException(String message) { super(message); }
    public InvalidNotificationException(String message, Throwable cause) { super(message, cause); }
}
