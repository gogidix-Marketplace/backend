package com.gogidix.sysadmin.performancemetrics.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePerformanceMetricCommand {

    private String id;
    private String tenantId;
}
