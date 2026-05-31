package com.gogidix.ecommerce.notification.shared.exception;

public class DuplicateNotificationException extends RuntimeException {
    private final String name;
    public DuplicateNotificationException(String name) { super("Duplicate Notification: " + name); this.name = name; }
    public String getName() { return name; }
}
