package com.gogidix.sales.forecast.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetForecastAdjustmentQuery {

    private String tenantId;
    private String id;
}
