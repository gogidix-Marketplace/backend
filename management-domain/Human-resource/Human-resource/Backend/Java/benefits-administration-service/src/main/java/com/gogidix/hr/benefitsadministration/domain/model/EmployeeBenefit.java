package com.gogidix.hr.benefitsadministration.domain.model;

import com.gogidix.hr.benefitsadministration.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Domain model for Employee Benefit summary
 * Represents an aggregated view of employee benefits
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBenefit extends BaseEntity {

    private String tenantId;
    private String employeeId;
    private String employeeName;
    private String department;
    private String benefitType;
    private String planName;
    private String coverageLevel;
    private LocalDate enrollmentDate;
    private LocalDate effectiveDate;
    private LocalDate terminationDate;
    private String status;
    private Boolean isActive;
}
