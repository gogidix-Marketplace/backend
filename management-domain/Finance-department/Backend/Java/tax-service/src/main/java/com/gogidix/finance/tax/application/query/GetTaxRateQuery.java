package com.gogidix.finance.tax.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTaxRateQuery {

    private String tenantId;
    private String id;
}
