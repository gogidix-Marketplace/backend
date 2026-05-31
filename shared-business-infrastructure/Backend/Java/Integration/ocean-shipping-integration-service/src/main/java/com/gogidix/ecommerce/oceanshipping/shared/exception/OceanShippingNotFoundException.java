package com.gogidix.ecommerce.oceanshipping.shared.exception;

public class OceanShippingNotFoundException extends RuntimeException {
    private final String id;
    public OceanShippingNotFoundException(String id) { super("OceanShipping not found: " + id); this.id = id; }
    public String getId() { return id; }
}
