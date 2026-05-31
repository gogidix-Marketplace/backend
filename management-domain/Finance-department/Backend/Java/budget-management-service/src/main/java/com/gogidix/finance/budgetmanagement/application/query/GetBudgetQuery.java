package com.gogidix.finance.budgetmanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBudgetQuery {

    private String tenantId;
    private String id;
}
