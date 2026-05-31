package com.gogidix.finance.budgettracking.application.dto.response;

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
 * Budget Transaction Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetTransactionResponseDto {

    private String id;

    private String transactionId;

    private String tenantId;

    private String budgetId;

    private String budgetCode;

    private String referenceType;

    private String referenceId;

    private TransactionTypeDto transactionType;

    private BigDecimal amount;

    private String currency;

    private String description;

    private TransactionStatusDto status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    private String category;

    private String department;

    private String costCenter;

    private String projectId;

    private String recordedBy;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    private String rejectionReason;

    private String relatedBudgetPeriod;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private List<String> tags;

    private String notes;

    private String correlationId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum TransactionTypeDto {
        ALLOCATION,
        COMMITMENT,
        EXPENDITURE,
        ADJUSTMENT,
        REVERSAL,
        TRANSFER_IN,
        TRANSFER_OUT,
        ENCUMBRANCE
    }

    public enum TransactionStatusDto {
        PENDING,
        RECORDED,
        APPROVED,
        REJECTED,
        REVERSED
    }
}
