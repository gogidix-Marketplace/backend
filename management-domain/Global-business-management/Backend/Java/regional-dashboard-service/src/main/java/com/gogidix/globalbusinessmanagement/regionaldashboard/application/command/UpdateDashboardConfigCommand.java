package com.gogidix.globalbusinessmanagement.regionaldashboard.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDashboardConfigCommand {

    private String id;
    private String tenantId;
}
