package com.gogidix.hr.benefitsadministration.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEmployeeBenefitQuery {

    private String tenantId;
    private String id;
}
