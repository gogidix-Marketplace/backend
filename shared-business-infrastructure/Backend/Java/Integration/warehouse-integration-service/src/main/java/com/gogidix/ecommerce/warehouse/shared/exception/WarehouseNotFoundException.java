package com.gogidix.ecommerce.warehouse.shared.exception;

public class WarehouseNotFoundException extends RuntimeException {
    private final String id;
    public WarehouseNotFoundException(String id) { super("Warehouse not found: " + id); this.id = id; }
    public String getId() { return id; }
}
