package com.gogidix.ecommerce.communication.shared.exception;

public class CommunicationNotFoundException extends RuntimeException {
    private final String id;
    public CommunicationNotFoundException(String id) { super("Communication not found: " + id); this.id = id; }
    public String getId() { return id; }
}
