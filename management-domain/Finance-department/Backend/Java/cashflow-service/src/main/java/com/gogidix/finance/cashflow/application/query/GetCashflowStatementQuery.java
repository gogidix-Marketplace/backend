package com.gogidix.finance.cashflow.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCashflowStatementQuery {

    private String tenantId;
    private String id;
}
