package com.gogidix.ecommerce.email.shared.exception;

public class EmailNotFoundException extends RuntimeException {
    private final String id;
    public EmailNotFoundException(String id) { super("Email not found: " + id); this.id = id; }
    public String getId() { return id; }
}
