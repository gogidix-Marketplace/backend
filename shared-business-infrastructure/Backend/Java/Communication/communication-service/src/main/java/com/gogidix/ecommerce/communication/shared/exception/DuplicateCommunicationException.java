package com.gogidix.ecommerce.communication.shared.exception;

public class DuplicateCommunicationException extends RuntimeException {
    private final String name;
    public DuplicateCommunicationException(String name) { super("Duplicate Communication: " + name); this.name = name; }
    public String getName() { return name; }
}
