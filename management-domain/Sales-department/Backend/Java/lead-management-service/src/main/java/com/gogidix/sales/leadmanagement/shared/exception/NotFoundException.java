package com.gogidix.sales.leadmanagement.shared.exception;

public class NotFoundException extends RuntimeException {

    private String code;
    private String resource;
    private String identifier;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String resource, String identifier) {
        super(resource + " not found: " + identifier);
        this.resource = resource;
        this.identifier = identifier;
    }

    public NotFoundException(String message, String code, String resource, String identifier) {
        super(message);
        this.code = code;
        this.resource = resource;
        this.identifier = identifier;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
}
