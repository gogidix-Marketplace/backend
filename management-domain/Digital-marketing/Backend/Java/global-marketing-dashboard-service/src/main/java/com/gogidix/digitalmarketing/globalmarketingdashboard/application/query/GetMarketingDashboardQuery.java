package com.gogidix.digitalmarketing.globalmarketingdashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMarketingDashboardQuery {

    private String tenantId;
    private String id;
}
