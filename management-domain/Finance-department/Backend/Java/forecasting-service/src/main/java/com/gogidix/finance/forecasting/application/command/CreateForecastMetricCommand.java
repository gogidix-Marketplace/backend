package com.gogidix.finance.forecasting.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateForecastMetricCommand {

    private String id;
    private String tenantId;
}
