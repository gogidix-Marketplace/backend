package com.gogidix.ecommerce.inventorysync.shared.exception;

public class InventorySyncNotFoundException extends RuntimeException {
    private final String id;
    public InventorySyncNotFoundException(String id) { super("InventorySync not found: " + id); this.id = id; }
    public String getId() { return id; }
}
