package com.gogidix.shared.warehousing.warehouseconfig.domain.exception;

/**
 * Exception thrown when config is not found
 */
public class ConfigNotFoundException extends RuntimeException {

    public ConfigNotFoundException(String message) {
        super(message);
    }
}
