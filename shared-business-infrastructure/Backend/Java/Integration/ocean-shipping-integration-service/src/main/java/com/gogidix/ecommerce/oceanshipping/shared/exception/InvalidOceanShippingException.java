package com.gogidix.ecommerce.oceanshipping.shared.exception;

public class InvalidOceanShippingException extends RuntimeException {
    public InvalidOceanShippingException(String message) { super(message); }
    public InvalidOceanShippingException(String message, Throwable cause) { super(message, cause); }
}
