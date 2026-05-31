package com.gogidix.finance.exchangerate.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRateSourceQuery {

    private String tenantId;
    private String id;
}
