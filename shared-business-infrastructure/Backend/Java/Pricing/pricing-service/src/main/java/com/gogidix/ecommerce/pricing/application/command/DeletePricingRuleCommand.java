package com.gogidix.ecommerce.pricing.application.command;

public record DeletePricingRuleCommand(
    String tenantId,
    String ruleId
) {}
