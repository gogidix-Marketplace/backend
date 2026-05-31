package com.gogidix.globalbusinessmanagement.regionalaggregation.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAggregatedMetricsQuery {

    private String tenantId;
    private String id;
}
