package com.gogidix.hr.globalcompliance.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateComplianceRequirementCommand {

    private String id;
    private String tenantId;
}
