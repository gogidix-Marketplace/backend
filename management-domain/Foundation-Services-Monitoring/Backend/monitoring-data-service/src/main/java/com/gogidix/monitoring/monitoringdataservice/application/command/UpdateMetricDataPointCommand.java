package com.gogidix.monitoring.monitoringdataservice.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMetricDataPointCommand {

    private String id;
    private String tenantId;
}
