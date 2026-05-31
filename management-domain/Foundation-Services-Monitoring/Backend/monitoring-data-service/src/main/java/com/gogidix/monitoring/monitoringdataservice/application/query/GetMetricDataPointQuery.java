package com.gogidix.monitoring.monitoringdataservice.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMetricDataPointQuery {

    private String tenantId;
    private String id;
}
