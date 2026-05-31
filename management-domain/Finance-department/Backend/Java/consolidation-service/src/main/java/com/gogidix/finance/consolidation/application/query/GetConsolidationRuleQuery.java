package com.gogidix.finance.consolidation.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetConsolidationRuleQuery {

    private String tenantId;
    private String id;
}
