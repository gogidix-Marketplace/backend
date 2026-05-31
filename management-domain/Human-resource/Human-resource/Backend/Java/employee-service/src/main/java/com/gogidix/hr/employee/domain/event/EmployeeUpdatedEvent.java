package com.gogidix.hr.employee.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Employee Updated Domain Event
 * Published when an employee's information is updated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdatedEvent implements DomainEvent {

    private String employeeId;
    private String tenantId;
    private String employeeNumber;
    private String employeeName;
    private String updateType;
    private String eventType;
    private Instant timestamp;
    private String updatedBy;
}
