package com.gogidix.finance.accountspayable.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPaymentQuery {

    private String tenantId;
    private String id;
}
