package com.gogidix.management.executive.financial.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for deleting (soft delete) a financials
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteFinancialDataCommand {

    @NotBlank(message = "FinancialData ID is required")
    private String financialDataId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
