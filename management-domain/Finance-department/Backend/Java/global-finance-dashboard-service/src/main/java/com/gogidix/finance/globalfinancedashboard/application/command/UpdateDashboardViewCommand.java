package com.gogidix.finance.globalfinancedashboard.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDashboardViewCommand {

    private String id;
    private String tenantId;
}
