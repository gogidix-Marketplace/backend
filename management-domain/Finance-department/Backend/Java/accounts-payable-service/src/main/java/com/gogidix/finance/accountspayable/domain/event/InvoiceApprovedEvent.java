package com.gogidix.finance.accountspayable.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceApprovedEvent {

    private String eventId;
    private String invoiceId;
    private String tenantId;
    private String vendorId;
    private String invoiceNumber;
    private BigDecimal amount;
    private String currency;
    private String approver;
    private String approvalLevel;
    private Instant timestamp;
    private String eventType;

    public static InvoiceApprovedEvent create(String invoiceId, String tenantId, String vendorId,
                                               String invoiceNumber, BigDecimal amount, String currency,
                                               String approver, String approvalLevel) {
        return InvoiceApprovedEvent.builder()
            .eventId(UUID.randomUUID().toString())
            .invoiceId(invoiceId)
            .tenantId(tenantId)
            .vendorId(vendorId)
            .invoiceNumber(invoiceNumber)
            .amount(amount)
            .currency(currency)
            .approver(approver)
            .approvalLevel(approvalLevel)
            .timestamp(Instant.now())
            .eventType("INVOICE_APPROVED")
            .build();
    }
}
