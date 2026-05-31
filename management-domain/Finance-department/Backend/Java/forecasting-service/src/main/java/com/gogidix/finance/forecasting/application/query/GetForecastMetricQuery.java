package com.gogidix.finance.forecasting.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetForecastMetricQuery {

    private String tenantId;
    private String id;
}
