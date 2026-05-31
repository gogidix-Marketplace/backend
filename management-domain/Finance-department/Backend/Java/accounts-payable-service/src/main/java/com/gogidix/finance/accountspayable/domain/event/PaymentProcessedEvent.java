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
public class PaymentProcessedEvent {

    private String eventId;
    private String paymentId;
    private String tenantId;
    private String vendorId;
    private BigDecimal amount;
    private String currency;
    private Instant timestamp;
    private String eventType;
}
