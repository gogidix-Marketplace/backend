package com.gogidix.ecommerce.analytics.shared.exception;

public class AnalyticsNotFoundException extends RuntimeException {
    private final String id;
    public AnalyticsNotFoundException(String id) { super("Analytics not found: " + id); this.id = id; }
    public String getId() { return id; }
}
