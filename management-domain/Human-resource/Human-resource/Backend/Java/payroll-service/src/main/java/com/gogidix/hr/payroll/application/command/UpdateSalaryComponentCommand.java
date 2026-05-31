package com.gogidix.hr.payroll.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSalaryComponentCommand {

    private String id;
    private String tenantId;
}
