package com.gogidix.sales.revenue.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.UUID;

/**
 * MRR Updated Domain Event
 * Published when Monthly Recurring Revenue is updated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MRRUpdatedEvent {

    @Builder.Default
    private String eventId = UUID.randomUUID().toString();
    @Builder.Default
    private String eventType = "MRR_UPDATED";
    @Builder.Default
    private Instant timestamp = Instant.now();
    private String correlationId;

    // MRR identification
    private String tenantId;
    private YearMonth reportingPeriod;
    private String calculationId;

    // MRR components
    private BigDecimal newBusinessMRR;
    private BigDecimal expansionMRR;
    private BigDecimal contractionMRR;
    private BigDecimal churnMRR;
    private BigDecimal totalMRR;

    // Previous values for comparison
    private BigDecimal previousMRR;
    private BigDecimal mrrChange;
    private BigDecimal mrrGrowthRate;

    // Additional context
    private String currency;
    private String territory;
    private String region;

    /**
     * Calculates MRR growth rate
     */
    public BigDecimal getMrrGrowthRate() {
        if (previousMRR == null || previousMRR.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return mrrChange.divide(previousMRR, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }
}
