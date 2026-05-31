package com.gogidix.management.executive.strategy.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for updating an existing dashboard
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStrategyCommand {

    @NotBlank(message = "Strategy ID is required")
    private String strategyId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private String name;
    private String description;
    private String layout;
    private StrategyStatus status;

    public enum StrategyStatus {
        DRAFT, ACTIVE, ARCHIVED
    }
}
