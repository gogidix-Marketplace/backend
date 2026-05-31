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
public class PaymentReceivedEvent {

    private String paymentId;
    private String tenantId;
    private String customerId;
    private String invoiceId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String transactionId;
    private String previousStatus;
    private String rejectionReason;
    private String eventType;
    private Instant timestamp;
}
