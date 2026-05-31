package com.gogidix.finance.globalfinancedashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDashboardWidgetQuery {

    private String tenantId;
    private String id;
}
