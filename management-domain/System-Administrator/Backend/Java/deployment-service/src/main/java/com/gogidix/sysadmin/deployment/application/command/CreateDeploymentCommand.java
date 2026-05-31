package com.gogidix.sysadmin.deployment.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeploymentCommand {

    private String id;
    private String tenantId;
}
