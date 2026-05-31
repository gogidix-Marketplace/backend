package com.gogidix.management.executive.strategy.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for deleting (soft delete) a dashboard
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteStrategyCommand {

    @NotBlank(message = "Strategy ID is required")
    private String strategyId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
