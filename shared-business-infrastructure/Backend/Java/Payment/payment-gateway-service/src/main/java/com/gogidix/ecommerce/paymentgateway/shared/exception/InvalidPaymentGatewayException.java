package com.gogidix.ecommerce.paymentgateway.shared.exception;

public class InvalidPaymentGatewayException extends RuntimeException {
    public InvalidPaymentGatewayException(String message) { super(message); }
    public InvalidPaymentGatewayException(String message, Throwable cause) { super(message, cause); }
}
