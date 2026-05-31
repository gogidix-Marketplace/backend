package com.gogidix.sysadmin.infrastructuremonitoring.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInfrastructureMonitoringCommand {

    private String id;
    private String tenantId;
}
