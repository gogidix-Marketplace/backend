package com.gogidix.ecommerce.pricing.shared.exception;

public class InvalidPricingRuleException extends RuntimeException {

    public InvalidPricingRuleException(String message) {
        super(message);
    }

    public InvalidPricingRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
