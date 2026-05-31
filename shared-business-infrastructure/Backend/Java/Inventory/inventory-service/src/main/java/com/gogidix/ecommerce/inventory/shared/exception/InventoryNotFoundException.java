package com.gogidix.ecommerce.inventory.shared.exception;

public class InventoryNotFoundException extends RuntimeException {
    private final String id;
    public InventoryNotFoundException(String id) { super("Inventory not found: " + id); this.id = id; }
    public String getId() { return id; }
}
