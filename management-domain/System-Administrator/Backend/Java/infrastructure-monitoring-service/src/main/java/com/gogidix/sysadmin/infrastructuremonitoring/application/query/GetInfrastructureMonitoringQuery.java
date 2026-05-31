package com.gogidix.sysadmin.infrastructuremonitoring.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetInfrastructureMonitoringQuery {

    private String tenantId;
    private String id;
}
