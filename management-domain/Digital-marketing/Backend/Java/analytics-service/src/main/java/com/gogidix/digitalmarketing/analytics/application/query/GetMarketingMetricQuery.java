package com.gogidix.digitalmarketing.analytics.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMarketingMetricQuery {

    private String tenantId;
    private String id;
}
