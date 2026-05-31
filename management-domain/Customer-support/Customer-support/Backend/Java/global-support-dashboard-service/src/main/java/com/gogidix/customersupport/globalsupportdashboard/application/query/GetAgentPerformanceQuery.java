package com.gogidix.customersupport.globalsupportdashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAgentPerformanceQuery {

    private String tenantId;
    private String id;
}
