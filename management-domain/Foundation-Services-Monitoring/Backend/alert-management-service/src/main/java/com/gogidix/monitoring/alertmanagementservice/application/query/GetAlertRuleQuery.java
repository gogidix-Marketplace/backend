package com.gogidix.monitoring.alertmanagementservice.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAlertRuleQuery {

    private String tenantId;
    private String id;
}
