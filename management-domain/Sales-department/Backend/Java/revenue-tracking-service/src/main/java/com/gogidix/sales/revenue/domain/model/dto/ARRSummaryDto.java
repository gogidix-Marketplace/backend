package com.gogidix.sales.revenue.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * ARR Summary DTO
 * Represents Annual Recurring Revenue summary
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ARRSummaryDto {

    private String tenantId;
    private YearMonth reportingPeriod;

    // ARR components
    private BigDecimal newBusinessARR;
    private BigDecimal expansionARR;
    private BigDecimal contractionARR;
    private BigDecimal churnARR;
    private BigDecimal totalARR;

    // Comparison
    private BigDecimal previousARR;
    private BigDecimal arrChange;
    private BigDecimal arrGrowthRate;

    // Derived metrics
    private BigDecimal averageContractValue;
    private Long customerCount;

    private String currency;
}
