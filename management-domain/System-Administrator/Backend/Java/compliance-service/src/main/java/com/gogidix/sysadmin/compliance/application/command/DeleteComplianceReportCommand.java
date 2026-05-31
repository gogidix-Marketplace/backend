package com.gogidix.sysadmin.compliance.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteComplianceReportCommand {

    private String id;
    private String tenantId;
}
