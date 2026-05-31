package com.gogidix.shared.courier.pricing.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query object for searching pricing rules
 * Supports multiple filter criteria
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingQuery {

    private String tenantId;

    private String ruleType;

    private String vehicleType;

    private String serviceType;

    private Boolean active;

    private String ruleName;

    private Integer priority;

    private Integer page;

    private Integer size;

    private String sortBy;

    private String sortDirection;
}
