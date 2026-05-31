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
public class BudgetTransactionRecordedEvent {

    private String transactionId;
    private String budgetId;
    private String budgetCode;
    private String tenantId;
    private BigDecimal amount;
    private String currency;
    private String transactionType;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
