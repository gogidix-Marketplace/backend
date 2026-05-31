package com.gogidix.hr.payroll.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollCreatedEvent {

    private String eventId;
    private String eventType;
    private String payrollId;
    private String tenantId;
    private String countryCode;
    private String payrollName;
    private YearMonth payrollPeriod;
    private BigDecimal totalAmount;
    private Integer employeeCount;
    private String createdBy;
    private Instant occurredAt;
    private String correlationId;
}
