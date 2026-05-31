package com.gogidix.finance.currency.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCurrencyPairQuery {

    private String tenantId;
    private String id;
}
