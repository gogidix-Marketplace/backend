package com.gogidix.sysadmin.deployment.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDeploymentQuery {

    private String tenantId;
    private String id;
}
