package com.gogidix.ecommerce.warehouse.shared.exception;

public class InvalidWarehouseException extends RuntimeException {
    public InvalidWarehouseException(String message) { super(message); }
    public InvalidWarehouseException(String message, Throwable cause) { super(message, cause); }
}
