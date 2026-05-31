package com.gogidix.ecommerce.oceanshipping.shared.exception;

public class DuplicateOceanShippingException extends RuntimeException {
    private final String name;
    public DuplicateOceanShippingException(String name) { super("Duplicate OceanShipping: " + name); this.name = name; }
    public String getName() { return name; }
}
