package com.gogidix.finance.ledger.infrastructure.persistence.mongodb;

import com.gogidix.finance.ledger.domain.event.JournalEntryEvent;
import com.gogidix.finance.ledger.domain.model.JournalEntry;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "journal_entries")
public class JournalEntryEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("journal_entry_id")
    private String journalEntryId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("entry_number")
    private String entryNumber;

    @Field("entry_date")
    private LocalDate entryDate;

    @Field("posting_date")
    private LocalDateTime postingDate;

    @Indexed
    @Field("status")
    private String status;

    @Field("description")
    private String description;

    @Field("reference")
    private String reference;

    @Field("source_document_type")
    private String sourceDocumentType;

    @Field("source_document_id")
    private String sourceDocumentId;

    @Field("source_module")
    private String sourceModule;

    @Field("period_id")
    private String periodId;

    @Field("fiscal_year")
    private Integer fiscalYear;

    @Field("fiscal_period")
    private Integer fiscalPeriod;

    @Field("created_by_user_id")
    private String createdByUserId;

    @Field("created_by_name")
    private String createdByName;

    @Field("approved_by_user_id")
    private String approvedByUserId;

    @Field("approved_at")
    private LocalDateTime approvedAt;

    @Field("posted_by_user_id")
    private String postedByUserId;

    @Field("posted_at")
    private LocalDateTime postedAt;

    @Field("total_debit")
    private BigDecimal totalDebit;

    @Field("total_credit")
    private BigDecimal totalCredit;

    @Field("currency")
    private String currency;

    @Field("exchange_rate")
    private BigDecimal exchangeRate;

    @Field("base_currency")
    private String baseCurrency;

    @Indexed
    @Field("is_reversed")
    private Boolean isReversed;

    @Field("reversed_by_entry_id")
    private String reversedByEntryId;

    @Field("reversal_date")
    private LocalDateTime reversalDate;

    @Field("requires_approval")
    private Boolean requiresApproval;

    @Field("lines")
    private List<JournalEntryLineEmbed> lines;

    @Field("domain_events")
    private List<JournalEntryEvent> domainEvents;

    @Field("attachment_urls")
    private List<String> attachmentUrls;

    @Field("notes")
    private String notes;

    @Field("batch_id")
    private String batchId;

    @Field("recurrence_id")
    private String recurrenceId;

    @Field("is_recurring")
    private Boolean isRecurring;

    @Field("reason_code")
    private String reasonCode;

    public JournalEntryEntity() {
    }

    public JournalEntryEntity(JournalEntry journalEntry) {
        this.journalEntryId = journalEntry.getJournalEntryId();
        this.tenantId = journalEntry.getTenantId();
        this.entryNumber = journalEntry.getEntryNumber();
        this.entryDate = journalEntry.getEntryDate();
        this.postingDate = journalEntry.getPostingDate();
        this.status = journalEntry.getStatus() != null ? journalEntry.getStatus().name() : null;
        this.description = journalEntry.getDescription();
        this.reference = journalEntry.getReference();
        this.sourceDocumentType = journalEntry.getSourceDocumentType();
        this.sourceDocumentId = journalEntry.getSourceDocumentId();
        this.sourceModule = journalEntry.getSourceModule();
        this.periodId = journalEntry.getPeriodId();
        this.fiscalYear = journalEntry.getFiscalYear();
        this.fiscalPeriod = journalEntry.getFiscalPeriod();
        this.createdByUserId = journalEntry.getCreatedByUserId();
        this.createdByName = journalEntry.getCreatedByName();
        this.approvedByUserId = journalEntry.getApprovedByUserId();
        this.approvedAt = journalEntry.getApprovedAt();
        this.postedByUserId = journalEntry.getPostedByUserId();
        this.postedAt = journalEntry.getPostedAt();
        this.totalDebit = journalEntry.getTotalDebit();
        this.totalCredit = journalEntry.getTotalCredit();
        this.currency = journalEntry.getCurrency();
        this.exchangeRate = journalEntry.getExchangeRate();
        this.baseCurrency = journalEntry.getBaseCurrency();
        this.isReversed = journalEntry.getIsReversed();
        this.reversedByEntryId = journalEntry.getReversedByEntryId();
        this.reversalDate = journalEntry.getReversalDate();
        this.requiresApproval = journalEntry.getRequiresApproval();
        this.lines = journalEntry.getLines() != null ?
                journalEntry.getLines().stream().map(JournalEntryLineEmbed::new).toList() : new ArrayList<>();
        this.domainEvents = journalEntry.getDomainEvents() != null ? new ArrayList<>(journalEntry.getDomainEvents()) : new ArrayList<>();
        this.attachmentUrls = journalEntry.getAttachmentUrls() != null ? new ArrayList<>(journalEntry.getAttachmentUrls()) : new ArrayList<>();
        this.notes = journalEntry.getNotes();
        this.batchId = journalEntry.getBatchId();
        this.recurrenceId = journalEntry.getRecurrenceId();
        this.isRecurring = journalEntry.getIsRecurring();
        this.reasonCode = journalEntry.getReasonCode();
    }

    public JournalEntry toDomainModel() {
        return JournalEntry.builder()
                .journalEntryId(this.journalEntryId)
                .tenantId(this.tenantId)
                .entryNumber(this.entryNumber)
                .entryDate(this.entryDate)
                .postingDate(this.postingDate)
                .status(this.status != null ? JournalEntry.JournalEntryStatus.valueOf(this.status) : null)
                .description(this.description)
                .reference(this.reference)
                .sourceDocumentType(this.sourceDocumentType)
                .sourceDocumentId(this.sourceDocumentId)
                .sourceModule(this.sourceModule)
                .periodId(this.periodId)
                .fiscalYear(this.fiscalYear)
                .fiscalPeriod(this.fiscalPeriod)
                .createdByUserId(this.createdByUserId)
                .createdByName(this.createdByName)
                .approvedByUserId(this.approvedByUserId)
                .approvedAt(this.approvedAt)
                .postedByUserId(this.postedByUserId)
                .postedAt(this.postedAt)
                .totalDebit(this.totalDebit)
                .totalCredit(this.totalCredit)
                .currency(this.currency)
                .exchangeRate(this.exchangeRate)
                .baseCurrency(this.baseCurrency)
                .isReversed(this.isReversed)
                .reversedByEntryId(this.reversedByEntryId)
                .reversalDate(this.reversalDate)
                .requiresApproval(this.requiresApproval)
                .lines(this.lines != null ? this.lines.stream().map(JournalEntryLineEmbed::toDomainModel).toList() : new ArrayList<>())
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .attachmentUrls(this.attachmentUrls != null ? new ArrayList<>(this.attachmentUrls) : new ArrayList<>())
                .notes(this.notes)
                .batchId(this.batchId)
                .recurrenceId(this.recurrenceId)
                .isRecurring(this.isRecurring)
                .reasonCode(this.reasonCode)
                .build();
    }

    public static class JournalEntryLineEmbed {
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

        public JournalEntryLineEmbed() {
        }

        public JournalEntryLineEmbed(JournalEntry.JournalEntryLine line) {
            this.lineId = line.getLineId();
            this.accountId = line.getAccountId();
            this.accountNumber = line.getAccountNumber();
            this.accountName = line.getAccountName();
            this.debitAmount = line.getDebitAmount();
            this.creditAmount = line.getCreditAmount();
            this.description = line.getDescription();
            this.costCenter = line.getCostCenter();
            this.department = line.getDepartment();
            this.projectId = line.getProjectId();
            this.taskId = line.getTaskId();
            this.reference = line.getReference();
            this.taxCode = line.getTaxCode();
            this.taxRate = line.getTaxRate();
            this.taxAmount = line.getTaxAmount();
            this.isTaxInclusive = line.getIsTaxInclusive();
            this.tags = line.getTags() != null ? new ArrayList<>(line.getTags()) : new ArrayList<>();
            this.sequenceNumber = line.getSequenceNumber();
        }

        public JournalEntry.JournalEntryLine toDomainModel() {
            return JournalEntry.JournalEntryLine.builder()
                    .lineId(this.lineId)
                    .accountId(this.accountId)
                    .accountNumber(this.accountNumber)
                    .accountName(this.accountName)
                    .debitAmount(this.debitAmount)
                    .creditAmount(this.creditAmount)
                    .description(this.description)
                    .costCenter(this.costCenter)
                    .department(this.department)
                    .projectId(this.projectId)
                    .taskId(this.taskId)
                    .reference(this.reference)
                    .taxCode(this.taxCode)
                    .taxRate(this.taxRate)
                    .taxAmount(this.taxAmount)
                    .isTaxInclusive(this.isTaxInclusive)
                    .tags(this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>())
                    .sequenceNumber(this.sequenceNumber)
                    .build();
        }

        // Getters and setters
        public String getLineId() { return lineId; }
        public void setLineId(String lineId) { this.lineId = lineId; }
        public String getAccountId() { return accountId; }
        public void setAccountId(String accountId) { this.accountId = accountId; }
        public String getAccountNumber() { return accountNumber; }
        public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
        public String getAccountName() { return accountName; }
        public void setAccountName(String accountName) { this.accountName = accountName; }
        public BigDecimal getDebitAmount() { return debitAmount; }
        public void setDebitAmount(BigDecimal debitAmount) { this.debitAmount = debitAmount; }
        public BigDecimal getCreditAmount() { return creditAmount; }
        public void setCreditAmount(BigDecimal creditAmount) { this.creditAmount = creditAmount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCostCenter() { return costCenter; }
        public void setCostCenter(String costCenter) { this.costCenter = costCenter; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getProjectId() { return projectId; }
        public void setProjectId(String projectId) { this.projectId = projectId; }
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getTaxCode() { return taxCode; }
        public void setTaxCode(String taxCode) { this.taxCode = taxCode; }
        public BigDecimal getTaxRate() { return taxRate; }
        public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
        public BigDecimal getTaxAmount() { return taxAmount; }
        public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
        public Boolean getIsTaxInclusive() { return isTaxInclusive; }
        public void setIsTaxInclusive(Boolean isTaxInclusive) { this.isTaxInclusive = isTaxInclusive; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public Integer getSequenceNumber() { return sequenceNumber; }
        public void setSequenceNumber(Integer sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getJournalEntryId() { return journalEntryId; }
    public void setJournalEntryId(String journalEntryId) { this.journalEntryId = journalEntryId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getEntryNumber() { return entryNumber; }
    public void setEntryNumber(String entryNumber) { this.entryNumber = entryNumber; }
    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
    public LocalDateTime getPostingDate() { return postingDate; }
    public void setPostingDate(LocalDateTime postingDate) { this.postingDate = postingDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getSourceDocumentType() { return sourceDocumentType; }
    public void setSourceDocumentType(String sourceDocumentType) { this.sourceDocumentType = sourceDocumentType; }
    public String getSourceDocumentId() { return sourceDocumentId; }
    public void setSourceDocumentId(String sourceDocumentId) { this.sourceDocumentId = sourceDocumentId; }
    public String getSourceModule() { return sourceModule; }
    public void setSourceModule(String sourceModule) { this.sourceModule = sourceModule; }
    public String getPeriodId() { return periodId; }
    public void setPeriodId(String periodId) { this.periodId = periodId; }
    public Integer getFiscalYear() { return fiscalYear; }
    public void setFiscalYear(Integer fiscalYear) { this.fiscalYear = fiscalYear; }
    public Integer getFiscalPeriod() { return fiscalPeriod; }
    public void setFiscalPeriod(Integer fiscalPeriod) { this.fiscalPeriod = fiscalPeriod; }
    public String getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(String createdByUserId) { this.createdByUserId = createdByUserId; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public String getApprovedByUserId() { return approvedByUserId; }
    public void setApprovedByUserId(String approvedByUserId) { this.approvedByUserId = approvedByUserId; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public String getPostedByUserId() { return postedByUserId; }
    public void setPostedByUserId(String postedByUserId) { this.postedByUserId = postedByUserId; }
    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }
    public BigDecimal getTotalDebit() { return totalDebit; }
    public void setTotalDebit(BigDecimal totalDebit) { this.totalDebit = totalDebit; }
    public BigDecimal getTotalCredit() { return totalCredit; }
    public void setTotalCredit(BigDecimal totalCredit) { this.totalCredit = totalCredit; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }
    public Boolean getIsReversed() { return isReversed; }
    public void setIsReversed(Boolean isReversed) { this.isReversed = isReversed; }
    public String getReversedByEntryId() { return reversedByEntryId; }
    public void setReversedByEntryId(String reversedByEntryId) { this.reversedByEntryId = reversedByEntryId; }
    public LocalDateTime getReversalDate() { return reversalDate; }
    public void setReversalDate(LocalDateTime reversalDate) { this.reversalDate = reversalDate; }
    public Boolean getRequiresApproval() { return requiresApproval; }
    public void setRequiresApproval(Boolean requiresApproval) { this.requiresApproval = requiresApproval; }
    public List<JournalEntryLineEmbed> getLines() { return lines; }
    public void setLines(List<JournalEntryLineEmbed> lines) { this.lines = lines; }
    public List<JournalEntryEvent> getDomainEvents() { return domainEvents; }
    public void setDomainEvents(List<JournalEntryEvent> domainEvents) { this.domainEvents = domainEvents; }
    public List<String> getAttachmentUrls() { return attachmentUrls; }
    public void setAttachmentUrls(List<String> attachmentUrls) { this.attachmentUrls = attachmentUrls; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public String getRecurrenceId() { return recurrenceId; }
    public void setRecurrenceId(String recurrenceId) { this.recurrenceId = recurrenceId; }
    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; }
    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }
}
