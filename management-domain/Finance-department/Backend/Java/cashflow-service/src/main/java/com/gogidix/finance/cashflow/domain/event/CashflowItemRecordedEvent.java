package com.gogidix.finance.cashflow.domain.event;

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
public class CashflowItemRecordedEvent {

    private String cashflowItemId;
    private String tenantId;
    private String type;
    private String category;
    private BigDecimal amount;
    private String currency;
    private String recordedBy;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
