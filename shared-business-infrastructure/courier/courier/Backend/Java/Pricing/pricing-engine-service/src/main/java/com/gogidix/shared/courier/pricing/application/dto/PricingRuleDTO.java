package com.gogidix.shared.courier.pricing.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for pricing rule data
 * Used for transferring pricing rule information between layers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRuleDTO {

    private String id;

    private String tenantId;

    private String ruleId;

    private String ruleName;

    private String description;

    private String ruleType;

    private Integer priority;

    private Boolean active;

    private String vehicleType;

    private String serviceType;

    private Map<String, Object> parameters;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;
}
