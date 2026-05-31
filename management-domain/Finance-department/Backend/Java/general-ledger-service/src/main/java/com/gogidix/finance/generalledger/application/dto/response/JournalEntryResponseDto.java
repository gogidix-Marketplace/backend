package com.gogidix.finance.generalledger.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Journal Entry Response DTO
 * Represents the response structure for journal entry operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryResponseDto {

    private String id;

    private String journalEntryId;

    private String tenantId;

    private String entryNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant postingDate;

    private JournalEntryStatusDto status;

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    private String postedByUserId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant postedAt;

    private BigDecimal totalDebit;

    private BigDecimal totalCredit;

    private String currency;

    private BigDecimal exchangeRate;

    private String baseCurrency;

    private Boolean isReversed;

    private String reversedByEntryId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant reversalDate;

    private List<JournalEntryLineDto> lines;

    private List<String> attachmentUrls;

    private String notes;

    private String batchId;

    private String recurrenceId;

    private Boolean isRecurring;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JournalEntryLineDto {
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

    public enum JournalEntryStatusDto {
        DRAFT,
        PENDING_APPROVAL,
        APPROVED,
        POSTED,
        REVERSED,
        CANCELLED
    }
}
