package com.gogidix.hr.employee.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Employee Promoted Domain Event
 * Published when an employee is promoted
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePromotedEvent implements DomainEvent {

    private String employeeId;
    private String tenantId;
    private String employeeNumber;
    private String employeeName;
    private String previousLevel;
    private String newLevel;
    private String previousPosition;
    private String newPosition;
    private Double previousSalary;
    private Double newSalary;
    private String reason;
    private String eventType;
    private Instant timestamp;
    private String approvedBy;

    public static EmployeePromotedEvent create(String employeeId, String tenantId,
                                                String employeeNumber, String employeeName,
                                                String previousLevel, String newLevel,
                                                String previousPosition, String newPosition,
                                                Double previousSalary, Double newSalary,
                                                String reason, String approvedBy) {
        return EmployeePromotedEvent.builder()
                .employeeId(employeeId)
                .tenantId(tenantId)
                .employeeNumber(employeeNumber)
                .employeeName(employeeName)
                .previousLevel(previousLevel)
                .newLevel(newLevel)
                .previousPosition(previousPosition)
                .newPosition(newPosition)
                .previousSalary(previousSalary)
                .newSalary(newSalary)
                .reason(reason)
                .eventType("EMPLOYEE_PROMOTED")
                .timestamp(Instant.now())
                .approvedBy(approvedBy)
                .build();
    }
}
