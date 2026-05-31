package com.gogidix.ecommerce.search.shared.exception;

public class SearchNotFoundException extends RuntimeException {
    private final String id;
    public SearchNotFoundException(String id) { super("Search not found: " + id); this.id = id; }
    public String getId() { return id; }
}
