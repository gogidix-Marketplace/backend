package com.gogidix.ecommerce.pricing.application.command;

public record DeactivatePricingRuleCommand(
    String tenantId,
    String ruleId
) {}
