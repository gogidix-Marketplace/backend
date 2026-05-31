package com.gogidix.ecommerce.pricing.application.command;

public record ActivatePricingRuleCommand(
    String tenantId,
    String ruleId
) {}
