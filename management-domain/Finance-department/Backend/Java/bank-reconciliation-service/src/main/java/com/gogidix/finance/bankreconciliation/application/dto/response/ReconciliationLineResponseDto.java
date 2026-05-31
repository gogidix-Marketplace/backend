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
 * Reconciliation Line Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReconciliationLineResponseDto {

    private String id;

    private String lineId;

    private String reconciliationId;

    private String accountId;

    private Integer lineNumber;

    private LineTypeDto lineType;

    private String bankTransactionId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate bankTransactionDate;

    private String bankDescription;

    private String bankReference;

    private BigDecimal bankAmount;

    private String bookTransactionId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookTransactionDate;

    private String bookDescription;

    private String bookReference;

    private BigDecimal bookAmount;

    private BigDecimal amountDifference;

    private MatchStatusDto matchStatus;

    private Double matchConfidence;

    private MatchedByDto matchedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant matchedAt;

    private String verifiedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant verifiedAt;

    private String discrepancyReason;

    private DiscrepancyCategoryDto discrepancyCategory;

    private ActionRequiredDto actionRequired;

    private String actionTaken;

    private String notes;

    private String currency;

    private Boolean autoMatched;

    private Boolean requiresManualReview;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum LineTypeDto {
        BANK_ONLY, BOOK_ONLY, MATCHED, DISCREPANCY, ADJUSTMENT
    }

    public enum MatchStatusDto {
        UNMATCHED, AUTO_MATCHED, MANUALLY_MATCHED, PENDING_REVIEW, DISCREPANCY, VERIFIED, REJECTED
    }

    public enum MatchedByDto {
        SYSTEM, USER, RULE, AI
    }

    public enum DiscrepancyCategoryDto {
        AMOUNT_MISMATCH, DATE_MISMATCH, MISSING_BANK_RECORD, MISSING_BOOK_RECORD,
        DUPLICATE_RECORD, CURRENCY_DIFFERENCE, TIMING_DIFFERENCE, OTHER
    }

    public enum ActionRequiredDto {
        NONE, INVESTIGATE, ADJUST_BOOK, ADJUST_BANK, CONTACT_BANK, DOCUMENT_EXCEPTION, IGNORE
    }
}
