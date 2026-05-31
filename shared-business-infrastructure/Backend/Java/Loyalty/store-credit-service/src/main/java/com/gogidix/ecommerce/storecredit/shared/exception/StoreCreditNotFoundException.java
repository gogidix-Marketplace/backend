package com.gogidix.ecommerce.storecredit.shared.exception;

public class StoreCreditNotFoundException extends RuntimeException {
    private final String id;
    public StoreCreditNotFoundException(String id) { super("StoreCredit not found: " + id); this.id = id; }
    public String getId() { return id; }
}
