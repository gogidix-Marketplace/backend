package com.gogidix.shared.warehousing.pricing.domain.exception;

/**
 * Exception thrown when pricing rule is not found
 */
public class PricingNotFoundException extends RuntimeException {

    public PricingNotFoundException(String message) {
        super(message);
    }

    public PricingNotFoundException(String id, String tenantId) {
        super(String.format("Pricing rule not found: id=%s, tenantId=%s", id, tenantId));
    }
}
