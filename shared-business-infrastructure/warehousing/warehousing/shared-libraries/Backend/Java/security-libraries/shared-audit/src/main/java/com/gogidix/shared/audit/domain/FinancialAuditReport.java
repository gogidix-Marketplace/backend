package com.gogidix.shared.audit.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * Domain object representing a financial audit report.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialAuditReport {
    
    private String reportId;
    private LocalDateTime generatedAt;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private BigDecimal totalTransactionAmount;
    private Long transactionCount;
    private BigDecimal averageTransactionAmount;
    private List<String> discrepancies;
    private List<String> recommendations;
    private String complianceStatus;
    
    /**
     * Validates the financial report is complete
     */
    public boolean isComplete() {
        return reportId != null && !reportId.trim().isEmpty() &&
               generatedAt != null &&
               periodStart != null &&
               periodEnd != null &&
               totalTransactionAmount != null &&
               transactionCount != null;
    }
}