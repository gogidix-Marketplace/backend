package com.gogidix.finance.ledger.domain.model;

import com.gogidix.finance.ledger.domain.event.JournalEntryEvent;
import com.gogidix.finance.ledger.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Journal Entry Domain Entity
 * Represents a double-entry journal entry
 * Multi-tenant with automatic balance validation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "journal_entries")
public class JournalEntry extends BaseEntity {

    @Indexed(unique = true)
    private String journalEntryId;

    @Indexed
    private String tenantId;

    @Indexed
    private String entryNumber;

    private LocalDate entryDate;

    private LocalDateTime postingDate;

    @Indexed
    private JournalEntryStatus status;

    private String description;

    private String reference;

    private String sourceDocumentType;

    private String sourceDocumentId;

    private String sourceModule;

    private String periodId;

    private Integer fiscalYear;

    private Integer fiscalPeriod;

    private String createdByUserId;

    private String createdByName;

    private String approvedByUserId;

    private LocalDateTime approvedAt;

    private String postedByUserId;

    private LocalDateTime postedAt;

    private BigDecimal totalDebit;

    private BigDecimal totalCredit;

    private String currency;

    private BigDecimal exchangeRate;

    private String baseCurrency;

    @Indexed
    private Boolean isReversed;

    private String reversedByEntryId;

    private LocalDateTime reversalDate;

    private Boolean requiresApproval;

    @Builder.Default
    private List<JournalEntryLine> lines = new ArrayList<>();

    @Builder.Default
    private List<JournalEntryEvent> domainEvents = new ArrayList<>();

    @Builder.Default
    private List<String> attachmentUrls = new ArrayList<>();

    private String notes;

    private String batchId;

    private String recurrenceId;

    private Boolean isRecurring;

    private String reasonCode;

    public enum JournalEntryStatus {
        DRAFT,
        PENDING_APPROVAL,
        APPROVED,
        POSTED,
        REVERSED,
        CANCELLED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JournalEntryLine {
        private String lineId;
        private String accountId;
        private String accountNumber;
        private String accountName;
        private BigDecimal debitAmount;
        private BigDecimal creditAmount;
        private String description;
        private String costCenter;
        private String department;
        private String projectId;
        private String taskId;
        private String reference;
        private String taxCode;
        private BigDecimal taxRate;
        private BigDecimal taxAmount;
        private Boolean isTaxInclusive;
        private List<String> tags;
        private Integer sequenceNumber;
    }

    /**
     * Creates a new journal entry
     */
    public static JournalEntry create(String tenantId, LocalDate entryDate, String description,
                                      String currency, String createdBy, String createdByName) {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(tenantId)
                .entryDate(entryDate)
                .description(description)
                .currency(currency)
                .status(JournalEntryStatus.DRAFT)
                .createdByUserId(createdBy)
                .createdByName(createdByName)
                .isReversed(false)
                .requiresApproval(false)
                .lines(new ArrayList<>())
                .domainEvents(new ArrayList<>())
                .attachmentUrls(new ArrayList<>())
                .totalDebit(BigDecimal.ZERO)
                .totalCredit(BigDecimal.ZERO)
                .build();

        entry.addDomainEvent(JournalEntryEvent.builder()
                .journalEntryId(entry.getJournalEntryId())
                .tenantId(tenantId)
                .eventType("JournalEntryCreated")
                .entryDate(entryDate)
                .createdBy(createdBy)
                .timestamp(LocalDateTime.now())
                .build());

        return entry;
    }

    /**
     * Adds a line to the journal entry
     */
    public void addLine(String accountId, String accountNumber, String accountName,
                       BigDecimal debitAmount, BigDecimal creditAmount, String description) {
        JournalEntryLine line = JournalEntryLine.builder()
                .lineId(generateLineId())
                .accountId(accountId)
                .accountNumber(accountNumber)
                .accountName(accountName)
                .debitAmount(debitAmount != null ? debitAmount : BigDecimal.ZERO)
                .creditAmount(creditAmount != null ? creditAmount : BigDecimal.ZERO)
                .description(description)
                .sequenceNumber(this.lines.size() + 1)
                .build();

        this.lines.add(line);
        recalculateTotals();
    }

    /**
     * Updates an existing line
     */
    public void updateLine(String lineId, BigDecimal debitAmount, BigDecimal creditAmount, String description) {
        JournalEntryLine line = findLineById(lineId);
        if (line == null) {
            throw new IllegalArgumentException("Line not found: " + lineId);
        }

        if (debitAmount != null) {
            line.setDebitAmount(debitAmount);
        }
        if (creditAmount != null) {
            line.setCreditAmount(creditAmount);
        }
        if (description != null) {
            line.setDescription(description);
        }

        recalculateTotals();
    }

    /**
     * Removes a line from the journal entry
     */
    public void removeLine(String lineId) {
        this.lines.removeIf(line -> line.getLineId().equals(lineId));
        // Re-sequence lines
        resequenceLines();
        recalculateTotals();
    }

    /**
     * Validates double-entry balance
     */
    public boolean isBalanced() {
        return totalDebit != null && totalCredit != null &&
               totalDebit.compareTo(totalCredit) == 0;
    }

    /**
     * Validates double-entry balance with tolerance
     */
    public boolean isBalanced(BigDecimal tolerance) {
        if (totalDebit == null || totalCredit == null) {
            return false;
        }
        BigDecimal difference = totalDebit.subtract(totalCredit).abs();
        return difference.compareTo(tolerance) <= 0;
    }

    /**
     * Submits for approval
     */
    public void submitForApproval() {
        if (this.status != JournalEntryStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft entries for approval");
        }

        validateForSubmission();

        this.status = JournalEntryStatus.PENDING_APPROVAL;

        addDomainEvent(JournalEntryEvent.builder()
                .journalEntryId(this.journalEntryId)
                .tenantId(this.tenantId)
                .eventType("JournalEntrySubmitted")
                .entryDate(this.entryDate)
                .createdBy(this.createdByUserId)
                .timestamp(LocalDateTime.now())
                .build());
    }

    /**
     * Approves the journal entry
     */
    public void approve(String approvedBy) {
        if (this.status != JournalEntryStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Can only approve pending entries");
        }

        if (!isBalanced()) {
            throw new IllegalStateException("Cannot approve unbalanced entry");
        }

        this.status = JournalEntryStatus.APPROVED;
        this.approvedByUserId = approvedBy;
        this.approvedAt = LocalDateTime.now();

        addDomainEvent(JournalEntryEvent.builder()
                .journalEntryId(this.journalEntryId)
                .tenantId(this.tenantId)
                .eventType("JournalEntryApproved")
                .entryDate(this.entryDate)
                .createdBy(approvedBy)
                .timestamp(LocalDateTime.now())
                .build());
    }

    /**
     * Posts the journal entry
     */
    public void post(String postedBy) {
        if (this.status != JournalEntryStatus.APPROVED) {
            throw new IllegalStateException("Can only post approved entries");
        }

        if (!isBalanced()) {
            throw new IllegalStateException("Cannot post unbalanced entry");
        }

        this.status = JournalEntryStatus.POSTED;
        this.postedByUserId = postedBy;
        this.postingDate = LocalDateTime.now();

        addDomainEvent(JournalEntryEvent.builder()
                .journalEntryId(this.journalEntryId)
                .tenantId(this.tenantId)
                .eventType("JournalEntryPosted")
                .entryDate(this.entryDate)
                .createdBy(postedBy)
                .timestamp(LocalDateTime.now())
                .totalDebit(this.totalDebit)
                .totalCredit(this.totalCredit)
                .build());
    }

    /**
     * Reverses the journal entry
     */
    public JournalEntry reverse(String reversalReason, String reversedBy, LocalDate reversalDate) {
        if (this.status != JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Can only reverse posted entries");
        }

        if (this.isReversed) {
            throw new IllegalStateException("Entry is already reversed");
        }

        this.isReversed = true;
        this.reversalDate = LocalDateTime.now();

        // Create reversing entry
        JournalEntry reversingEntry = JournalEntry.create(
                this.tenantId,
                reversalDate,
                "Reversal of " + this.entryNumber + ": " + reversalReason,
                this.currency,
                reversedBy,
                reversedBy
        );

        reversingEntry.setReference(this.journalEntryId);
        reversingEntry.setSourceDocumentType("REVERSAL");

        // Add reversing lines
        for (JournalEntryLine line : this.lines) {
            reversingEntry.addLine(
                    line.getAccountId(),
                    line.getAccountNumber(),
                    line.getAccountName(),
                    line.getCreditAmount(), // Swap debit and credit
                    line.getDebitAmount(),
                    "Reversal: " + line.getDescription()
            );
        }

        reversingEntry.requiresApproval = false;

        addDomainEvent(JournalEntryEvent.builder()
                .journalEntryId(this.journalEntryId)
                .tenantId(this.tenantId)
                .eventType("JournalEntryReversed")
                .entryDate(this.entryDate)
                .createdBy(reversedBy)
                .timestamp(LocalDateTime.now())
                .reversalReason(reversalReason)
                .build());

        return reversingEntry;
    }

    /**
     * Cancels the journal entry
     */
    public void cancel(String reason) {
        if (this.status == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot cancel posted entries. Use reverse instead.");
        }

        this.status = JournalEntryStatus.CANCELLED;
        this.notes = reason;

        addDomainEvent(JournalEntryEvent.builder()
                .journalEntryId(this.journalEntryId)
                .tenantId(this.tenantId)
                .eventType("JournalEntryCancelled")
                .entryDate(this.entryDate)
                .createdBy(this.createdByUserId)
                .timestamp(LocalDateTime.now())
                .cancellationReason(reason)
                .build());
    }

    /**
     * Returns to draft status
     */
    public void returnToDraft() {
        if (this.status == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot return posted entry to draft");
        }
        if (this.status == JournalEntryStatus.CANCELLED) {
            throw new IllegalStateException("Cannot return cancelled entry to draft");
        }

        this.status = JournalEntryStatus.DRAFT;
        this.approvedByUserId = null;
        this.approvedAt = null;
    }

    /**
     * Validates entry for submission
     */
    private void validateForSubmission() {
        if (this.lines == null || this.lines.isEmpty()) {
            throw new IllegalStateException("Journal entry must have at least one line");
        }

        if (this.lines.size() < 2) {
            throw new IllegalStateException("Journal entry must have at least two lines for double-entry");
        }

        if (!isBalanced(new BigDecimal("0.01"))) {
            throw new IllegalStateException("Journal entry must be balanced (debits = credits)");
        }

        if (totalDebit.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("Journal entry total cannot be zero");
        }
    }

    /**
     * Recalculates totals
     */
    private void recalculateTotals() {
        this.totalDebit = this.lines.stream()
                .map(JournalEntryLine::getDebitAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalCredit = this.lines.stream()
                .map(JournalEntryLine::getCreditAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Re-sequences lines after removal
     */
    private void resequenceLines() {
        for (int i = 0; i < this.lines.size(); i++) {
            this.lines.get(i).setSequenceNumber(i + 1);
        }
    }

    /**
     * Finds a line by ID
     */
    private JournalEntryLine findLineById(String lineId) {
        return this.lines.stream()
                .filter(line -> line.getLineId().equals(lineId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Generates a unique line ID
     */
    private String generateLineId() {
        return "LINE-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    /**
     * Adds a domain event
     */
    public void addDomainEvent(JournalEntryEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    /**
     * Clears domain events
     */
    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Checks if entry can be modified
     */
    public boolean canModify() {
        return this.status == JournalEntryStatus.DRAFT;
    }

    /**
     * Gets line count
     */
    public int getLineCount() {
        return this.lines != null ? this.lines.size() : 0;
    }

    /**
     * Sets fiscal period
     */
    public void setFiscalPeriod(Integer fiscalYear, Integer fiscalPeriod) {
        this.fiscalYear = fiscalYear;
        this.fiscalPeriod = fiscalPeriod;
    }
}
