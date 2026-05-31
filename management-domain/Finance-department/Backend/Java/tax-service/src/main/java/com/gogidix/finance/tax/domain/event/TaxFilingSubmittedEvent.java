package com.gogidix.finance.tax.domain.event;

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
public class TaxFilingSubmittedEvent {

    private String filingId;
    private String tenantId;
    private Object taxType;
    private Object filingType;
    private Object jurisdiction;
    private YearMonth filingPeriod;
    private String submittedBy;
    private String approvedBy;
    private BigDecimal netAmount;
    private BigDecimal taxDue;
    private BigDecimal taxRefund;
    private String paymentReference;
    private String acknowledgementNumber;
    private String reason;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
