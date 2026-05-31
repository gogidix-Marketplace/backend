package com.gogidix.ecommerce.email.shared.exception;

public class DuplicateEmailException extends RuntimeException {
    private final String name;
    public DuplicateEmailException(String name) { super("Duplicate Email: " + name); this.name = name; }
    public String getName() { return name; }
}
