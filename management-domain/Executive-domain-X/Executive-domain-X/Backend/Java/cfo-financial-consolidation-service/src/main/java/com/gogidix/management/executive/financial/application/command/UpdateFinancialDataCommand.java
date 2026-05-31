package com.gogidix.management.executive.financial.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for updating an existing financials
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFinancialDataCommand {

    @NotBlank(message = "FinancialData ID is required")
    private String financialDataId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private String name;
    private String description;
    private String layout;
    private FinancialDataStatus status;

    public enum FinancialDataStatus {
        DRAFT, ACTIVE, ARCHIVED
    }
}
