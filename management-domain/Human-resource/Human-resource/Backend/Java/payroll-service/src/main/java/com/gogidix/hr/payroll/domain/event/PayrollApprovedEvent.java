package com.gogidix.hr.payroll.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollApprovedEvent {

    private String eventId;
    private String eventType;
    private String payrollId;
    private String tenantId;
    private String countryCode;
    private YearMonth payrollPeriod;
    private String approvedBy;
    private Instant occurredAt;
    private String correlationId;
}
