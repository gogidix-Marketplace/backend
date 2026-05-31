package com.gogidix.finance.bankreconciliation.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetReconciliationLineQuery {

    private String tenantId;
    private String id;
}
