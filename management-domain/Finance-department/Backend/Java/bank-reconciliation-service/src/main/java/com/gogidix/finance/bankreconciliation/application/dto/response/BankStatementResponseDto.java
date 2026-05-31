package com.gogidix.finance.bankreconciliation.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Bank Statement Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankStatementResponseDto {

    private String id;

    private String statementId;

    private String accountId;

    private String accountNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate statementDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private BigDecimal openingBalance;

    private BigDecimal closingBalance;

    private String currency;

    private ImportStatusDto importStatus;

    private ImportSourceDto importSource;

    private String fileReference;

    private Integer transactionCount;

    private BigDecimal totalDebits;

    private BigDecimal totalCredits;

    private List<String> importErrors;

    private List<String> importWarnings;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant processedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant validatedAt;

    private Boolean reconciled;

    private String reconciliationId;

    private StatementTypeDto statementType;

    private String bankReference;

    private List<StatementTransactionDto> transactions;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum ImportStatusDto {
        PENDING, PROCESSING, COMPLETED, FAILED, PARTIAL_SUCCESS, VALIDATING
    }

    public enum ImportSourceDto {
        MANUAL_UPLOAD, BANK_API, SFTP, EMAIL, BATCH_IMPORT, AUTOMATIC_FEED
    }

    public enum StatementTypeDto {
        STATEMENT, INTERIM, FINAL, CORRECTION
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatementTransactionDto {
        private String transactionId;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate transactionDate;
        private String description;
        private String reference;
        private BigDecimal amount;
        private TransactionTypeDto transactionType;
        private String category;
        private Boolean isReconciled;
        private String reconciliationLineId;
    }

    public enum TransactionTypeDto {
        DEBIT, CREDIT, TRANSFER_IN, TRANSFER_OUT, INTEREST, FEE, TAX
    }
}
