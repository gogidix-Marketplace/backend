package com.gogidix.finance.accountsreceivable.domain.event;

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
public class InvoiceGeneratedEvent {

    private String invoiceId;
    private String tenantId;
    private String customerId;
    private String invoiceNumber;
    private BigDecimal totalAmount;
    private BigDecimal balanceDue;
    private BigDecimal amountPaid;
    private BigDecimal writeOffAmount;
    private String currency;
    private Instant dueDate;
    private String eventType;
    private Instant timestamp;
}
