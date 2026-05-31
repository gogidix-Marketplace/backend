package com.gogidix.finance.bankreconciliation.domain.event;

import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

@Data
public class ReconciliationCompletedEvent {

    private String eventId;
    private String reconciliationId;
    private String tenantId;
    private String accountId;
    private String accountNumber;
    private String statementId;
    private LocalDate reconciliationDate;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private BigDecimal startingBalance;
    private BigDecimal endingBalance;
    private BigDecimal bookBalance;
    private BigDecimal bankBalance;
    private BigDecimal difference;
    private Boolean isBalanced;
    private Integer lineCount;
    private Integer matchedCount;
    private Integer unmatchedCount;
    private Integer discrepancyCount;
    private String reconciledBy;
    private String reconciliationMethod;
    private Boolean autoReconciled;
    private Integer completionPercentage;
    private Instant timestamp;
    private String eventType;

    public ReconciliationCompletedEvent() {}

    public static ReconciliationCompletedEvent create(
            String reconciliationId, String tenantId, String accountId, String accountNumber,
            String statementId, LocalDate reconciliationDate, LocalDate periodStart, LocalDate periodEnd,
            BigDecimal startingBalance, BigDecimal endingBalance, BigDecimal bookBalance,
            BigDecimal bankBalance, BigDecimal difference, Boolean isBalanced,
            Integer lineCount, Integer matchedCount, Integer unmatchedCount, Integer discrepancyCount,
            String reconciledBy, String reconciliationMethod, Boolean autoReconciled,
            Integer completionPercentage) {
        ReconciliationCompletedEvent event = new ReconciliationCompletedEvent();
        event.eventId = UUID.randomUUID().toString();
        event.reconciliationId = reconciliationId;
        event.tenantId = tenantId;
        event.accountId = accountId;
        event.accountNumber = accountNumber;
        event.statementId = statementId;
        event.reconciliationDate = reconciliationDate;
        event.periodStart = periodStart;
        event.periodEnd = periodEnd;
        event.startingBalance = startingBalance;
        event.endingBalance = endingBalance;
        event.bookBalance = bookBalance;
        event.bankBalance = bankBalance;
        event.difference = difference;
        event.isBalanced = isBalanced;
        event.lineCount = lineCount;
        event.matchedCount = matchedCount;
        event.unmatchedCount = unmatchedCount;
        event.discrepancyCount = discrepancyCount;
        event.reconciledBy = reconciledBy;
        event.reconciliationMethod = reconciliationMethod;
        event.autoReconciled = autoReconciled;
        event.completionPercentage = completionPercentage;
        event.timestamp = Instant.now();
        event.eventType = "RECONCILIATION_COMPLETED";
        return event;
    }
}
