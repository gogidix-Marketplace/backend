package com.gogidix.sysadmin.environment.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteEnvironmentCommand {

    private String id;
    private String tenantId;
}
