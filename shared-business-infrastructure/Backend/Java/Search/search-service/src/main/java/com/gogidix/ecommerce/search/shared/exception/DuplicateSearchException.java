package com.gogidix.ecommerce.search.shared.exception;

public class DuplicateSearchException extends RuntimeException {
    private final String name;
    public DuplicateSearchException(String name) { super("Duplicate Search: " + name); this.name = name; }
    public String getName() { return name; }
}
