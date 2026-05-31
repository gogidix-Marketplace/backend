package com.gogidix.management.executive.financial.application.query;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query for listing financial data by tenant
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListFinancialDataQuery {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private String ownerId;

    private FinancialDataStatus status;

    @Min(value = 0, message = "Page must be non-negative")
    private int page = 0;

    @Min(value = 1, message = "Size must be positive")
    private int size = 20;

    public enum FinancialDataStatus {
        DRAFT, ACTIVE, ARCHIVED, ALL
    }
}
