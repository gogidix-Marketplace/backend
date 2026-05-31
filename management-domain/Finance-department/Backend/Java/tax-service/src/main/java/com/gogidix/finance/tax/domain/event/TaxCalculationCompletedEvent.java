package com.gogidix.finance.tax.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxCalculationCompletedEvent {

    private String calculationId;
    private String tenantId;
    private String transactionId;
    private String taxType;
    private String jurisdiction;
    private BigDecimal totalTax;
    private BigDecimal netAmount;
    private BigDecimal effectiveRate;
    private String verifiedBy;
    private String reason;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
