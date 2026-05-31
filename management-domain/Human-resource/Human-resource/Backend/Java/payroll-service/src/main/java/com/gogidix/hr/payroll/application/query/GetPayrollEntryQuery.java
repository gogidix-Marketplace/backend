package com.gogidix.hr.payroll.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPayrollEntryQuery {

    private String tenantId;
    private String id;
}
