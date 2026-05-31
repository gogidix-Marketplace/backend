package com.gogidix.ecommerce.pricing.shared.exception;

public class PricingRuleNotFoundException extends RuntimeException {

    private final String ruleId;

    public PricingRuleNotFoundException(String ruleId) {
        super("Pricing rule not found: " + ruleId);
        this.ruleId = ruleId;
    }

    public PricingRuleNotFoundException(String message, String ruleId) {
        super(message);
        this.ruleId = ruleId;
    }

    public String getRuleId() {
        return ruleId;
    }
}
