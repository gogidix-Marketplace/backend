package com.gogidix.ecommerce.inventory.shared.exception;

public class DuplicateInventoryException extends RuntimeException {
    private final String name;
    public DuplicateInventoryException(String name) { super("Duplicate Inventory: " + name); this.name = name; }
    public String getName() { return name; }
}
