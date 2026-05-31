package com.gogidix.ecommerce.sms.shared.exception;

public class SmsNotFoundException extends RuntimeException {
    private final String id;
    public SmsNotFoundException(String id) { super("Sms not found: " + id); this.id = id; }
    public String getId() { return id; }
}
