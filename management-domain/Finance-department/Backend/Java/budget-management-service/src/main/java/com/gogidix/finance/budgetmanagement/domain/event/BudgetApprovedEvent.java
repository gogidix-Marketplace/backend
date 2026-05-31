package com.gogidix.finance.budgetmanagement.domain.event;

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
public class BudgetApprovedEvent {

    private String budgetId;
    private String tenantId;
    private String name;
    private String department;
    private String fiscalYear;
    private BigDecimal totalAllocated;
    private String approvedBy;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
