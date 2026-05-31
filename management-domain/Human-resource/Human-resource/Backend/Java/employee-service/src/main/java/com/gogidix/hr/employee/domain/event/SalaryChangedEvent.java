package com.gogidix.hr.employee.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Salary Changed Domain Event
 * Published when an employee's salary is updated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryChangedEvent implements DomainEvent {

    private String employeeId;
    private String tenantId;
    private String employeeNumber;
    private String employeeName;
    private Double previousSalary;
    private Double newSalary;
    private Double changeAmount;
    private Double changePercentage;
    private String reason;
    private String eventType;
    private Instant timestamp;
    private String approvedBy;

    public static SalaryChangedEvent create(String employeeId, String tenantId,
                                            String employeeNumber, String employeeName,
                                            Double previousSalary, Double newSalary,
                                            String reason, String approvedBy) {
        Double changeAmount = null;
        Double changePercentage = null;
        if (previousSalary != null && newSalary != null) {
            changeAmount = newSalary - previousSalary;
            if (previousSalary != 0) {
                changePercentage = (changeAmount / previousSalary) * 100;
            }
        }

        return SalaryChangedEvent.builder()
                .employeeId(employeeId)
                .tenantId(tenantId)
                .employeeNumber(employeeNumber)
                .employeeName(employeeName)
                .previousSalary(previousSalary)
                .newSalary(newSalary)
                .changeAmount(changeAmount)
                .changePercentage(changePercentage)
                .reason(reason)
                .eventType("SALARY_CHANGED")
                .timestamp(Instant.now())
                .approvedBy(approvedBy)
                .build();
    }
}
