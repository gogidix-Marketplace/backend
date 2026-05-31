package com.gogidix.sysadmin.infrastructuremonitoring.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMonitoringAlertCommand {

    private String id;
    private String tenantId;
}
