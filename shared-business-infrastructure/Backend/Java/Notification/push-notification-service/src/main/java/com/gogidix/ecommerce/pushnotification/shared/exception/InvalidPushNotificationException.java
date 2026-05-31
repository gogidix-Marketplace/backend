package com.gogidix.ecommerce.pushnotification.shared.exception;

public class InvalidPushNotificationException extends RuntimeException {
    public InvalidPushNotificationException(String message) { super(message); }
    public InvalidPushNotificationException(String message, Throwable cause) { super(message, cause); }
}
