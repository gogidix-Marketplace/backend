package com.gogidix.dashboard.core.domain.port.in;

import lombok.Builder;
import lombok.Data;

/**
 * Input port query for retrieving a KPI.
 */
@Data
@Builder
public class GetKPIQuery {

    private String kpiId;

    private String code;

    private String tenantId;

    @Builder.Default
    private Boolean includeHistoricalValues = false;

    @Builder.Default
    private Boolean includeTargets = false;

    @Builder.Default
    private Integer historicalDays = 30;
}
