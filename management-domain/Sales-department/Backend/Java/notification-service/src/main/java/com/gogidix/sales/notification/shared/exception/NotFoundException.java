package com.gogidix.sales.notification.shared.exception;

import lombok.Getter;

/**
 * Not Found Exception
 * Thrown when a requested resource is not found
 */
@Getter
public class NotFoundException extends RuntimeException {

    private final String resource;
    private final String identifier;

    public NotFoundException(String resource, String identifier) {
        super(String.format("%s not found: %s", resource, identifier));
        this.resource = resource;
        this.identifier = identifier;
    }

    public NotFoundException(String resource) {
        super(String.format("%s not found", resource));
        this.resource = resource;
        this.identifier = null;
    }
}
