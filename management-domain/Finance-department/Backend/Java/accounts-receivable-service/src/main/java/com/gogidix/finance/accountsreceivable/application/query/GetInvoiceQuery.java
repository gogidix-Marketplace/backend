package com.gogidix.finance.accountsreceivable.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetInvoiceQuery {

    private String tenantId;
    private String id;
}
