package com.gogidix.finance.bankreconciliation.domain.event;

import com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine.DiscrepancyCategory;
import com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine.ActionRequired;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

@Data
public class ReconciliationDiscrepancyEvent {

    private String eventId;
    private String reconciliationId;
    private String tenantId;
    private String accountId;
    private String accountNumber;
    private String lineId;
    private LocalDate bankTransactionDate;
    private String bankTransactionId;
    private String bankDescription;
    private BigDecimal bankAmount;
    private String bookTransactionId;
    private String bookDescription;
    private BigDecimal bookAmount;
    private BigDecimal amountDifference;
    private DiscrepancyCategory discrepancyCategory;
    private String discrepancyReason;
    private ActionRequired actionRequired;
    private Boolean requiresManualReview;
    private String currency;
    private Instant timestamp;
    private String eventType;

    public ReconciliationDiscrepancyEvent() {}

    public static ReconciliationDiscrepancyEvent create(
            String reconciliationId, String tenantId, String accountId, String accountNumber,
            String lineId, LocalDate bankTransactionDate, String bankTransactionId,
            String bankDescription, BigDecimal bankAmount, String bookTransactionId,
            String bookDescription, BigDecimal bookAmount, BigDecimal amountDifference,
            DiscrepancyCategory discrepancyCategory, String discrepancyReason,
            ActionRequired actionRequired, Boolean requiresManualReview, String currency) {
        ReconciliationDiscrepancyEvent event = new ReconciliationDiscrepancyEvent();
        event.eventId = UUID.randomUUID().toString();
        event.reconciliationId = reconciliationId;
        event.tenantId = tenantId;
        event.accountId = accountId;
        event.accountNumber = accountNumber;
        event.lineId = lineId;
        event.bankTransactionDate = bankTransactionDate;
        event.bankTransactionId = bankTransactionId;
        event.bankDescription = bankDescription;
        event.bankAmount = bankAmount;
        event.bookTransactionId = bookTransactionId;
        event.bookDescription = bookDescription;
        event.bookAmount = bookAmount;
        event.amountDifference = amountDifference;
        event.discrepancyCategory = discrepancyCategory;
        event.discrepancyReason = discrepancyReason;
        event.actionRequired = actionRequired;
        event.requiresManualReview = requiresManualReview;
        event.currency = currency;
        event.timestamp = Instant.now();
        event.eventType = "RECONCILIATION_DISCREPANCY";
        return event;
    }
}
