package com.gogidix.ecommerce.communication.shared.exception;

public class InvalidCommunicationException extends RuntimeException {
    public InvalidCommunicationException(String message) { super(message); }
    public InvalidCommunicationException(String message, Throwable cause) { super(message, cause); }
}
