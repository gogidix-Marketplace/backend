package com.gogidix.digitalmarketing.globalmarketingdashboard.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMarketingDashboardCommand {

    private String id;
    private String tenantId;
}
