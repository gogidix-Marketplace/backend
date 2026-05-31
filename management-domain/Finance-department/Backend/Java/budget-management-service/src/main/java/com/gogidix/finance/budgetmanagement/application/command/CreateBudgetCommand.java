package com.gogidix.finance.budgetmanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBudgetCommand {

    private String id;
    private String tenantId;
}
