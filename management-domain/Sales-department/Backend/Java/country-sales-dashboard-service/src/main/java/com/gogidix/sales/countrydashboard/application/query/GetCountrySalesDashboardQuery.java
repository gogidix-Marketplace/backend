package com.gogidix.sales.countrydashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCountrySalesDashboardQuery {

    private String tenantId;
    private String id;
}
