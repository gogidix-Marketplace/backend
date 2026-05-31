package com.gogidix.ecommerce.notification.shared.exception;

public class NotificationNotFoundException extends RuntimeException {
    private final String id;
    public NotificationNotFoundException(String id) { super("Notification not found: " + id); this.id = id; }
    public String getId() { return id; }
}
