package com.gogidix.ecommerce.pricing.shared.exception;

public class DuplicatePricingRuleException extends RuntimeException {

    private final String ruleCode;

    public DuplicatePricingRuleException(String ruleCode) {
        super("Pricing rule already exists with code: " + ruleCode);
        this.ruleCode = ruleCode;
    }

    public String getRuleCode() {
        return ruleCode;
    }
}
