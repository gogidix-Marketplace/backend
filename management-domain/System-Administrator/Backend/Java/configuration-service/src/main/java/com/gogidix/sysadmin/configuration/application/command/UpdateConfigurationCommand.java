package com.gogidix.sysadmin.configuration.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConfigurationCommand {

    private String id;
    private String tenantId;
}
