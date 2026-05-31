package com.gogidix.hr.payroll.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayslipGeneratedEvent {

    private String eventId;
    private String eventType;
    private String payslipId;
    private String payrollId;
    private String employeeId;
    private String tenantId;
    private BigDecimal netPay;
    private String currency;
    private Instant occurredAt;
    private String correlationId;
}
