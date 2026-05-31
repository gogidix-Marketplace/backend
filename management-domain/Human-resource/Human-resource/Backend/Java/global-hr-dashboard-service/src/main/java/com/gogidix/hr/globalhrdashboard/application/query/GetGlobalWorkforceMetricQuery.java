package com.gogidix.hr.globalhrdashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetGlobalWorkforceMetricQuery {

    private String tenantId;
    private String id;
}
