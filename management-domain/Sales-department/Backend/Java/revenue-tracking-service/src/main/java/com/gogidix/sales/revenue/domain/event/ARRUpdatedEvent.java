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
 * ARR Updated Domain Event
 * Published when Annual Recurring Revenue is updated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ARRUpdatedEvent {

    @Builder.Default
    private String eventId = UUID.randomUUID().toString();
    @Builder.Default
    private String eventType = "ARR_UPDATED";
    @Builder.Default
    private Instant timestamp = Instant.now();
    private String correlationId;

    // ARR identification
    private String tenantId;
    private YearMonth reportingPeriod;
    private String calculationId;

    // ARR components
    private BigDecimal newBusinessARR;
    private BigDecimal expansionARR;
    private BigDecimal contractionARR;
    private BigDecimal churnARR;
    private BigDecimal totalARR;

    // Previous values for comparison
    private BigDecimal previousARR;
    private BigDecimal arrChange;
    private BigDecimal arrGrowthRate;

    // Derived metrics
    private BigDecimal averageContractValue;
    private Long customerCount;

    // Additional context
    private String currency;
    private String territory;
    private String region;

    /**
     * Calculates ARR growth rate
     */
    public BigDecimal getArrGrowthRate() {
        if (previousARR == null || previousARR.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return arrChange.divide(previousARR, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }
}
