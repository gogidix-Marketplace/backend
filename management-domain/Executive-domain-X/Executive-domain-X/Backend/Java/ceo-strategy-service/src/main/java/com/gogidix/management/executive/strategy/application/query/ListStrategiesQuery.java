package com.gogidix.management.executive.strategy.application.query;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query for listing dashboards by tenant
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListStrategiesQuery {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private String ownerId;

    private StrategyStatus status;

    @Min(value = 0, message = "Page must be non-negative")
    private int page = 0;

    @Min(value = 1, message = "Size must be positive")
    private int size = 20;

    public enum StrategyStatus {
        DRAFT, ACTIVE, ARCHIVED, ALL
    }
}
