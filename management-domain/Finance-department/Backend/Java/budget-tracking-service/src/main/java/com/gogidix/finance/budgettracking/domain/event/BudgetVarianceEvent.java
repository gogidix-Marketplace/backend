package com.gogidix.finance.budgettracking.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetVarianceEvent {

    private String varianceId;
    private String budgetId;
    private String budgetCode;
    private String tenantId;
    private BigDecimal varianceAmount;
    private BigDecimal variancePercentage;
    private String varianceStatus;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
