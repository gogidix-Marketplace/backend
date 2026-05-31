package com.gogidix.ecommerce.airfreight.shared.exception;

public class DuplicateAirFreightException extends RuntimeException {
    private final String name;
    public DuplicateAirFreightException(String name) { super("Duplicate AirFreight: " + name); this.name = name; }
    public String getName() { return name; }
}
