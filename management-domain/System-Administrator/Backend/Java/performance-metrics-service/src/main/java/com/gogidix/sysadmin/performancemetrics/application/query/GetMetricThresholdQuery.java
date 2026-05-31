package com.gogidix.sysadmin.performancemetrics.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMetricThresholdQuery {

    private String tenantId;
    private String id;
}
