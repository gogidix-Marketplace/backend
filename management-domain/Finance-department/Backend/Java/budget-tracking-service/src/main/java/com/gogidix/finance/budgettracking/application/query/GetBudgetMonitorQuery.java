package com.gogidix.finance.budgettracking.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBudgetMonitorQuery {

    private String tenantId;
    private String id;
}
