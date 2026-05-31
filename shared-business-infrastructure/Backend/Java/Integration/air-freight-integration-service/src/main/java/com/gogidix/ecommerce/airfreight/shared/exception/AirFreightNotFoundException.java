package com.gogidix.ecommerce.airfreight.shared.exception;

public class AirFreightNotFoundException extends RuntimeException {
    private final String id;
    public AirFreightNotFoundException(String id) { super("AirFreight not found: " + id); this.id = id; }
    public String getId() { return id; }
}
