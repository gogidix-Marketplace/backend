package com.gogidix.hr.employee.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Employee Terminated Domain Event
 * Published when an employee is terminated or resigns
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTerminatedEvent implements DomainEvent {

    private String employeeId;
    private String tenantId;
    private String employeeNumber;
    private String employeeName;
    private String terminationReason;
    private String terminationCategory;
    private LocalDate lastWorkingDay;
    private boolean rehireEligible;
    private String eventType;
    private Instant timestamp;
    private String processedBy;

    public static EmployeeTerminatedEvent create(String employeeId, String tenantId,
                                                   String employeeNumber, String employeeName,
                                                   String reason, String category,
                                                   LocalDate lastWorkingDay, String processedBy) {
        return EmployeeTerminatedEvent.builder()
                .employeeId(employeeId)
                .tenantId(tenantId)
                .employeeNumber(employeeNumber)
                .employeeName(employeeName)
                .terminationReason(reason)
                .terminationCategory(category)
                .lastWorkingDay(lastWorkingDay)
                .eventType("EMPLOYEE_TERMINATED")
                .timestamp(Instant.now())
                .processedBy(processedBy)
                .rehireEligible(true)
                .build();
    }
}
