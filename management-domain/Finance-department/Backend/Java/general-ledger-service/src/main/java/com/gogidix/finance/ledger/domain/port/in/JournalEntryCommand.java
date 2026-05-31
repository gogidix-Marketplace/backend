package com.gogidix.finance.ledger.domain.port.in;

import com.gogidix.finance.ledger.domain.model.JournalEntry;
import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Journal Entry Commands (Input Port)
 * Defines the input commands for journal entry operations
 */
public interface JournalEntryCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateJournalEntryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotNull(message = "Entry date is required")
        private LocalDate entryDate;

        @NotBlank(message = "Description is required")
        private String description;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotBlank(message = "Created by is required")
        private String createdBy;

        private String createdByName;

        private String reference;

        private String sourceDocumentType;

        private String sourceDocumentId;

        private String sourceModule;

        @NotNull(message = "Lines are required")
        @NotEmpty(message = "At least one line is required")
        private List<JournalEntryLineDto> lines;

        private String periodId;

        private Integer fiscalYear;

        private Integer fiscalPeriod;

        private Boolean requiresApproval;

        private String notes;

        private String batchId;

        private BigDecimal exchangeRate;

        private String baseCurrency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class JournalEntryLineDto {
        @NotBlank(message = "Account ID is required")
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

        private List<String> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateJournalEntryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Journal Entry ID is required")
        private String journalEntryId;

        private LocalDate entryDate;

        private String description;

        private String reference;

        private String notes;

        private List<JournalEntryLineDto> lines;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddLineCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Journal Entry ID is required")
        private String journalEntryId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        private String accountNumber;

        private String accountName;

        private BigDecimal debitAmount;

        private BigDecimal creditAmount;

        private String description;

        private String costCenter;

        private String department;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateLineCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Journal Entry ID is required")
        private String journalEntryId;

        @NotBlank(message = "Line ID is required")
        private String lineId;

        private BigDecimal debitAmount;

        private BigDecimal creditAmount;

        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveLineCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Journal Entry ID is required")
        private String journalEntryId;

        @NotBlank(message = "Line ID is required")
        private String lineId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SubmitForApprovalCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Journal Entry ID is required")
        private String journalEntryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveJournalEntryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Journal Entry ID is required")
        private String journalEntryId;

        @NotBlank(message = "Approved by is required")
        private String approvedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PostJournalEntryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Journal Entry ID is required")
        private String journalEntryId;

        @NotBlank(message = "Posted by is required")
        private String postedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReverseJournalEntryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Journal Entry ID is required")
        private String journalEntryId;

        @NotBlank(message = "Reversal reason is required")
        private String reversalReason;

        @NotBlank(message = "Reversed by is required")
        private String reversedBy;

        @NotNull(message = "Reversal date is required")
        private LocalDate reversalDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelJournalEntryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Journal Entry ID is required")
        private String journalEntryId;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteJournalEntryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Journal Entry ID is required")
        private String journalEntryId;
    }
}
