package com.gogidix.finance.accountsreceivable.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerQuery {

    private String tenantId;
    private String id;
}
