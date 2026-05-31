package com.gogidix.ecommerce.pushnotification.shared.exception;

public class PushNotificationNotFoundException extends RuntimeException {
    private final String id;
    public PushNotificationNotFoundException(String id) { super("PushNotification not found: " + id); this.id = id; }
    public String getId() { return id; }
}
