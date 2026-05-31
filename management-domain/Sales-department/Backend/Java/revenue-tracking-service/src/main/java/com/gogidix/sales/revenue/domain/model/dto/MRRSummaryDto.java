package com.gogidix.sales.revenue.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * MRR Summary DTO
 * Represents Monthly Recurring Revenue summary
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MRRSummaryDto {

    private String tenantId;
    private YearMonth reportingPeriod;

    // MRR components
    private BigDecimal newBusinessMRR;
    private BigDecimal expansionMRR;
    private BigDecimal contractionMRR;
    private BigDecimal churnMRR;
    private BigDecimal totalMRR;

    // Comparison
    private BigDecimal previousMRR;
    private BigDecimal mrrChange;
    private BigDecimal mrrGrowthRate;

    private String currency;
}
