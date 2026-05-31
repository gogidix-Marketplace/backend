package com.gogidix.finance.bankreconciliation.domain.event;

import com.gogidix.finance.bankreconciliation.domain.model.BankStatement.ImportSource;
import com.gogidix.finance.bankreconciliation.domain.model.BankStatement.StatementType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

@Data
public class BankStatementImportedEvent {

    private String eventId;
    private String statementId;
    private String tenantId;
    private String accountId;
    private String accountNumber;
    private LocalDate statementDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal openingBalance;
    private BigDecimal closingBalance;
    private String currency;
    private ImportSource importSource;
    private String fileReference;
    private Integer transactionCount;
    private BigDecimal totalDebits;
    private BigDecimal totalCredits;
    private boolean balancesValid;
    private boolean hasImportWarnings;
    private int importWarningCount;
    private String importedBy;
    private StatementType statementType;
    private String bankReference;
    private Instant timestamp;
    private String eventType;

    public BankStatementImportedEvent() {}

    public static BankStatementImportedEvent create(
            String statementId, String tenantId, String accountId, String accountNumber,
            LocalDate statementDate, LocalDate startDate, LocalDate endDate,
            BigDecimal openingBalance, BigDecimal closingBalance, String currency,
            ImportSource importSource, String fileReference, Integer transactionCount,
            BigDecimal totalDebits, BigDecimal totalCredits, boolean balancesValid,
            boolean hasImportWarnings, int importWarningCount, String importedBy,
            StatementType statementType, String bankReference) {
        BankStatementImportedEvent event = new BankStatementImportedEvent();
        event.eventId = UUID.randomUUID().toString();
        event.statementId = statementId;
        event.tenantId = tenantId;
        event.accountId = accountId;
        event.accountNumber = accountNumber;
        event.statementDate = statementDate;
        event.startDate = startDate;
        event.endDate = endDate;
        event.openingBalance = openingBalance;
        event.closingBalance = closingBalance;
        event.currency = currency;
        event.importSource = importSource;
        event.fileReference = fileReference;
        event.transactionCount = transactionCount;
        event.totalDebits = totalDebits;
        event.totalCredits = totalCredits;
        event.balancesValid = balancesValid;
        event.hasImportWarnings = hasImportWarnings;
        event.importWarningCount = importWarningCount;
        event.importedBy = importedBy;
        event.statementType = statementType;
        event.bankReference = bankReference;
        event.timestamp = Instant.now();
        event.eventType = "BANK_STATEMENT_IMPORTED";
        return event;
    }
}
