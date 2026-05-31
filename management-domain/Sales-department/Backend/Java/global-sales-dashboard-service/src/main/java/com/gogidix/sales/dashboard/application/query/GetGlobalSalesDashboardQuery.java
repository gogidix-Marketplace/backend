package com.gogidix.sales.dashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetGlobalSalesDashboardQuery {

    private String tenantId;
    private String id;
}
