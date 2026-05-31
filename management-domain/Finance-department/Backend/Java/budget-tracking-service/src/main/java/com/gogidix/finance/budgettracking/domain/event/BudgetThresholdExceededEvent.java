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
public class BudgetThresholdExceededEvent {

    private String budgetId;
    private String budgetCode;
    private String monitorId;
    private String tenantId;
    private String thresholdType;
    private BigDecimal thresholdValue;
    private BigDecimal currentValue;
    private String level;
    private Instant timestamp;
    private String eventId;
}
