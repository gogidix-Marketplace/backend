package com.gogidix.shared.warehousing.serialization.domain.exception;

/**
 * Exception thrown when attempting to create a serialized item with a duplicate serial number
 */
public class DuplicateSerialNumberException extends RuntimeException {

    public DuplicateSerialNumberException(String serialNumber) {
        super(String.format("Serialized item with serial number '%s' already exists", serialNumber));
    }
}
