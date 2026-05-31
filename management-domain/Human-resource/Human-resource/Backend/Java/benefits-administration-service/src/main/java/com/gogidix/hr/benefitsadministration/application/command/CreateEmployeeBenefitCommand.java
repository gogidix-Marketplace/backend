package com.gogidix.hr.benefitsadministration.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeBenefitCommand {

    private String id;
    private String tenantId;
}
