package com.gogidix.ecommerce.pushnotification.shared.exception;

public class DuplicatePushNotificationException extends RuntimeException {
    private final String name;
    public DuplicatePushNotificationException(String name) { super("Duplicate PushNotification: " + name); this.name = name; }
    public String getName() { return name; }
}
