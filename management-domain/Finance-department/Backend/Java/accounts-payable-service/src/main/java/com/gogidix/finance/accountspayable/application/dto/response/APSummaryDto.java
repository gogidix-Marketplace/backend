package com.gogidix.finance.accountspayable.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Accounts Payable Summary DTO
 * Contains comprehensive AP statistics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APSummaryDto {

    // Vendor Summary
    private Long totalVendors;

    private Long activeVendors;

    // Invoice Summary
    private Long totalInvoices;

    private Long pendingInvoices;

    private Long approvedInvoices;

    private Long overdueInvoices;

    private BigDecimal pendingAmount;

    private BigDecimal approvedAmount;

    private BigDecimal overdueAmount;

    private BigDecimal totalOutstanding;

    // Payment Summary
    private Long totalPayments;

    private Long pendingPayments;

    private Long scheduledPayments;

    private Long completedPayments;

    private Long failedPayments;

    private BigDecimal totalPaymentAmount;

    // Optional: Vendor-specific fields
    private String vendorId;

    private String vendorName;

    private String vendorStatus;
}
