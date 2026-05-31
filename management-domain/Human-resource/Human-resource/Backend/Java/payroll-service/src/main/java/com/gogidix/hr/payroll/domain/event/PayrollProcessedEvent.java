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
public class PayrollProcessedEvent {

    private String eventId;
    private String eventType;
    private String payrollId;
    private String tenantId;
    private String countryCode;
    private YearMonth payrollPeriod;
    private BigDecimal totalGrossPay;
    private BigDecimal totalNetPay;
    private BigDecimal totalTaxes;
    private BigDecimal totalDeductions;
    private Integer employeeCount;
    private String processedBy;
    private Instant occurredAt;
    private String correlationId;
}
