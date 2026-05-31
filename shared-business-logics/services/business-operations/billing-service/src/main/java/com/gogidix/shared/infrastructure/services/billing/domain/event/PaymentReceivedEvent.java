package com.gogidix.shared.infrastructure.services.billing.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Domain event published when a payment is received.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReceivedEvent {
    private String paymentId;
    private String tenantId;
    private String invoiceId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String status;
    private Instant occurredAt;
}
