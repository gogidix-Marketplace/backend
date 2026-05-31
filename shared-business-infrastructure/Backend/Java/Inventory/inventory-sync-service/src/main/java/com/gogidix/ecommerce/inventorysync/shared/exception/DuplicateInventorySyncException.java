package com.gogidix.ecommerce.inventorysync.shared.exception;

public class DuplicateInventorySyncException extends RuntimeException {
    private final String name;
    public DuplicateInventorySyncException(String name) { super("Duplicate InventorySync: " + name); this.name = name; }
    public String getName() { return name; }
}
