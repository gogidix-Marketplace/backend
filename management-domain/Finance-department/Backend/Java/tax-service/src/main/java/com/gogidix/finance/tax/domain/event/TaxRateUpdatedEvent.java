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
public class TaxRateUpdatedEvent {

    private String taxRateId;
    private String tenantId;
    private String taxCode;
    private Object taxType;
    private Object jurisdiction;
    private BigDecimal oldRate;
    private BigDecimal newRate;
    private java.time.LocalDate effectiveDate;
    private String changedBy;
    private String status;
    private Integer version;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
