package com.gogidix.management.executive.financial.application.query;

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
public class GetFinancialDataQuery {

    @NotBlank(message = "FinancialData ID is required")
    private String financialDataId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
