package com.gogidix.shared.infrastructure.services.billing.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Domain event published when an invoice is created.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCreatedEvent {
    private String invoiceId;
    private String tenantId;
    private String subscriptionId;
    private String invoiceNumber;
    private BigDecimal amount;
    private String currency;
    private Instant dueDate;
    private Instant occurredAt;
}
