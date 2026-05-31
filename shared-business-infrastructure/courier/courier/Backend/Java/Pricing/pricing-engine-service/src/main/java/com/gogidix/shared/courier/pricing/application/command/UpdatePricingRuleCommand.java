package com.gogidix.shared.courier.pricing.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Command for updating an existing pricing rule
 * Allows partial updates to pricing configuration
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePricingRuleCommand {

    @NotBlank(message = "Rule ID is required")
    private String ruleId;

    private String ruleName;

    private String description;

    private String ruleType;

    @Positive(message = "Priority must be positive")
    private Integer priority;

    private Boolean active;

    private String vehicleType;

    private String serviceType;

    private Map<String, Object> parameters;
}
