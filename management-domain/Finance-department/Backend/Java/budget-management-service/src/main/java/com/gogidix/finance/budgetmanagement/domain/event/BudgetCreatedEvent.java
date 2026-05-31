package com.gogidix.finance.budgetmanagement.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetCreatedEvent {

    private String budgetId;
    private String tenantId;
    private String name;
    private String department;
    private String fiscalYear;
    private String period;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
