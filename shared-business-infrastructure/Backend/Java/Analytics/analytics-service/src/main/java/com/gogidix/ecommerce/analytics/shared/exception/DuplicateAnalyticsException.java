package com.gogidix.ecommerce.analytics.shared.exception;

public class DuplicateAnalyticsException extends RuntimeException {
    private final String name;
    public DuplicateAnalyticsException(String name) { super("Duplicate Analytics: " + name); this.name = name; }
    public String getName() { return name; }
}
