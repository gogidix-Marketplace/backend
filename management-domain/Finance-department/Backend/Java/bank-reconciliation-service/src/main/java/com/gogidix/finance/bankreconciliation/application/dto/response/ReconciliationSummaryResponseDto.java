package com.gogidix.finance.bankreconciliation.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Reconciliation Summary Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReconciliationSummaryResponseDto {

    private Long totalReconciliations;

    private Long completedCount;

    private Long pendingCount;

    private Long balancedCount;

    private BigDecimal totalDiscrepancyAmount;

    private Integer totalLines;

    private Integer totalMatched;

    private Long pendingStatements;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodEnd;

    private String accountId;

    private String accountNumber;
}
