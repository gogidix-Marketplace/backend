package com.gogidix.ecommerce.pushnotification.shared.exception;

public class PushNotificationDuplicateException extends RuntimeException {

    public PushNotificationDuplicateException(String message) {
        super(message);
    }
}