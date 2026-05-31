package com.gogidix.sysadmin.deployment.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDeploymentCommand {

    private String id;
    private String tenantId;
}
