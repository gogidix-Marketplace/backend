package com.gogidix.finance.compliance.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateComplianceCheckCommand {

    private String id;
    private String tenantId;
}
