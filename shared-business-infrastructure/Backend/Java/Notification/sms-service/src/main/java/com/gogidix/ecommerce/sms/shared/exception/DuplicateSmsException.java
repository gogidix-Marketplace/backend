package com.gogidix.ecommerce.sms.shared.exception;

public class DuplicateSmsException extends RuntimeException {
    private final String name;
    public DuplicateSmsException(String name) { super("Duplicate Sms: " + name); this.name = name; }
    public String getName() { return name; }
}
