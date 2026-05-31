package com.gogidix.sales.dealmanagement.shared.exception;

public class NotFoundException extends RuntimeException {

    private String resource;
    private String identifier;
    private String code;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String resource, String identifier) {
        super(resource + " not found: " + identifier);
        this.resource = resource;
        this.identifier = identifier;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
