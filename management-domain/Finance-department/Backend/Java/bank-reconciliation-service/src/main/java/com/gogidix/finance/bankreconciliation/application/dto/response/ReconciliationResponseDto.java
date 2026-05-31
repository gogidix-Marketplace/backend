package com.gogidix.finance.bankreconciliation.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Reconciliation Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReconciliationResponseDto {

    private String id;

    private String reconciliationId;

    private String accountId;

    private String accountNumber;

    private String statementId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reconciliationDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodEnd;

    private ReconciliationStatusDto status;

    private BigDecimal startingBalance;

    private BigDecimal endingBalance;

    private BigDecimal bookBalance;

    private BigDecimal bankBalance;

    private BigDecimal difference;

    private BigDecimal tolerance;

    private Boolean isBalanced;

    private String reconciledBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant reconciledAt;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    private Integer lineCount;

    private Integer matchedCount;

    private Integer unmatchedCount;

    private Integer discrepancyCount;

    private String notes;

    private Boolean autoReconciled;

    private ReconciliationMethodDto reconciliationMethod;

    private Integer completionPercentage;

    private String errorMessage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum ReconciliationStatusDto {
        PENDING, IN_PROGRESS, COMPLETED, FAILED, CANCELLED, AWAITING_APPROVAL, APPROVED
    }

    public enum ReconciliationMethodDto {
        AUTOMATIC, MANUAL, HYBRID, RULE_BASED, AI_ASSISTED
    }
}
