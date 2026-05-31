package com.gogidix.ecommerce.airfreight.shared.exception;

public class InvalidAirFreightException extends RuntimeException {
    public InvalidAirFreightException(String message) { super(message); }
    public InvalidAirFreightException(String message, Throwable cause) { super(message, cause); }
}
