package com.gogidix.finance.cashflow.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCashflowStatementCommand {

    private String id;
    private String tenantId;
}
