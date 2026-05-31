package com.gogidix.sysadmin.incidentmanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIncidentCommand {

    private String id;
    private String tenantId;
}
