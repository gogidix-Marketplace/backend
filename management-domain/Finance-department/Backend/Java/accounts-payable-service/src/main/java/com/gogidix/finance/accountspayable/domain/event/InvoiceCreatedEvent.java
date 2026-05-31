package com.gogidix.finance.accountspayable.domain.event;

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
public class InvoiceCreatedEvent {

    private String eventId;
    private String invoiceId;
    private String tenantId;
    private String vendorId;
    private String invoiceNumber;
    private BigDecimal amount;
    private String currency;
    private Instant timestamp;
    private String eventType;
}
