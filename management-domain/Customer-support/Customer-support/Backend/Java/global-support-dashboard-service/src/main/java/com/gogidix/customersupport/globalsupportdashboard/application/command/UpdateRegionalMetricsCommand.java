package com.gogidix.customersupport.globalsupportdashboard.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRegionalMetricsCommand {

    private String id;
    private String tenantId;
}
