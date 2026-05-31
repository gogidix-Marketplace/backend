package com.gogidix.finance.globalfinancedashboard.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDashboardWidgetCommand {

    private String id;
    private String tenantId;
}
