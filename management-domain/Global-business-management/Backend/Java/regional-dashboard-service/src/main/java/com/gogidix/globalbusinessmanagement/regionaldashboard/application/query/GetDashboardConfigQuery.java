package com.gogidix.globalbusinessmanagement.regionaldashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDashboardConfigQuery {

    private String tenantId;
    private String id;
}
