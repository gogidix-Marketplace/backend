package com.gogidix.ecommerce.storecredit.shared.exception;

public class DuplicateStoreCreditException extends RuntimeException {
    private final String name;
    public DuplicateStoreCreditException(String name) { super("Duplicate StoreCredit: " + name); this.name = name; }
    public String getName() { return name; }
}
