package com.gogidix.ecommerce.warehouse.shared.exception;

public class DuplicateWarehouseException extends RuntimeException {
    private final String name;
    public DuplicateWarehouseException(String name) { super("Duplicate Warehouse: " + name); this.name = name; }
    public String getName() { return name; }
}
