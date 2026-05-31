package com.gogidix.sales.forecast.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteForecastLineItemCommand {

    private String id;
    private String tenantId;
}
