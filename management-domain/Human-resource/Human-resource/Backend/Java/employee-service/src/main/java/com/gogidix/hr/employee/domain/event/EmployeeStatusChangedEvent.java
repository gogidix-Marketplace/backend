package com.gogidix.hr.employee.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Employee Status Changed Domain Event
 * Published when an employee's status changes
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeStatusChangedEvent implements DomainEvent {

    private String employeeId;
    private String tenantId;
    private String employeeNumber;
    private String employeeName;
    private String previousStatus;
    private String newStatus;
    private String reason;
    private String eventType;
    private Instant timestamp;
    private String changedBy;

    public static EmployeeStatusChangedEvent create(String employeeId, String tenantId,
                                                     String employeeNumber, String employeeName,
                                                     String previousStatus, String newStatus,
                                                     String reason, String changedBy) {
        return EmployeeStatusChangedEvent.builder()
                .employeeId(employeeId)
                .tenantId(tenantId)
                .employeeNumber(employeeNumber)
                .employeeName(employeeName)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .reason(reason)
                .eventType("EMPLOYEE_STATUS_CHANGED")
                .timestamp(Instant.now())
                .changedBy(changedBy)
                .build();
    }
}
