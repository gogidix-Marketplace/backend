package com.gogidix.management.executive.strategy.application.query;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query for fetching a specific strategy by ID
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetStrategyQuery {

    @NotBlank(message = "Strategy ID is required")
    private String strategyId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
