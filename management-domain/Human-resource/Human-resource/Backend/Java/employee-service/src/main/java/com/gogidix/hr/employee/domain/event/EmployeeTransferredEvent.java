package com.gogidix.hr.employee.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Employee Transferred Domain Event
 * Published when an employee is transferred to a new department
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTransferredEvent implements DomainEvent {

    private String employeeId;
    private String tenantId;
    private String employeeNumber;
    private String employeeName;
    private String previousDepartment;
    private String newDepartment;
    private String previousPosition;
    private String newPosition;
    private String previousManagerId;
    private String newManagerId;
    private String reason;
    private String eventType;
    private Instant timestamp;
    private String approvedBy;

    public static EmployeeTransferredEvent create(String employeeId, String tenantId,
                                                   String employeeNumber, String employeeName,
                                                   String previousDepartment, String newDepartment,
                                                   String previousPosition, String newPosition,
                                                   String reason, String approvedBy) {
        return EmployeeTransferredEvent.builder()
                .employeeId(employeeId)
                .tenantId(tenantId)
                .employeeNumber(employeeNumber)
                .employeeName(employeeName)
                .previousDepartment(previousDepartment)
                .newDepartment(newDepartment)
                .previousPosition(previousPosition)
                .newPosition(newPosition)
                .reason(reason)
                .eventType("EMPLOYEE_TRANSFERRED")
                .timestamp(Instant.now())
                .approvedBy(approvedBy)
                .build();
    }
}
