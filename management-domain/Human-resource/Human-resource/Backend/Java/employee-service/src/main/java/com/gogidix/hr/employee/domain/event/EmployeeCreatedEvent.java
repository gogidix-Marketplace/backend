package com.gogidix.hr.employee.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Employee Created Domain Event
 * Published when a new employee is created
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreatedEvent implements DomainEvent {

    private String employeeId;
    private String tenantId;
    private String employeeNumber;
    private String employeeName;
    private String eventType;
    private Instant timestamp;
    private Map<String, Object> metadata;

    public static EmployeeCreatedEvent create(String employeeId, String tenantId,
                                               String employeeNumber, String employeeName) {
        return EmployeeCreatedEvent.builder()
                .employeeId(employeeId)
                .tenantId(tenantId)
                .employeeNumber(employeeNumber)
                .employeeName(employeeName)
                .eventType("EMPLOYEE_CREATED")
                .timestamp(Instant.now())
                .build();
    }
}
