package com.gogidix.finance.cashflow.application.dto.response;

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
 * Cashflow Item Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashflowItemResponseDto {

    private String id;

    private String cashflowItemId;

    private String tenantId;

    private String recordedBy;

    private String reference;

    private CashflowTypeDto type;

    private CashflowCategoryDto category;

    private BigDecimal amount;

    private String currency;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate settledDate;

    private String description;

    private String counterparty;

    private String account;

    private String costCenter;

    private String projectId;

    private ItemStatusDto status;

    private Boolean recurring;

    private RecurringFrequencyDto recurringFrequency;

    private String parentRecurringItemId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant expectedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant settledAt;

    private String paymentMethod;

    private String bankReference;

    private String invoiceReference;

    private BigDecimal taxAmount;

    private BigDecimal netAmount;

    private List<String> tags;

    private String notes;

    private String linkedExpenseId;

    private String linkedRevenueId;

    private BigDecimal allocationPercentage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum CashflowTypeDto {
        INFLOW, OUTFLOW
    }

    public enum CashflowCategoryDto {
        OPERATING_REVENUE, INVESTMENT_RETURN, LOAN_PROCEEDS, CAPITAL_CONTRIBUTION,
        REFUND_RECEIVED, INTEREST_INCOME, DIVIDEND_INCOME, OTHER_INFLOW,
        OPERATING_EXPENSE, PAYROLL, TAX_PAYMENT, LOAN_REPAYMENT, CAPITAL_EXPENDITURE,
        DIVIDEND_PAYMENT, INTEREST_PAYMENT, REFUND_ISSUED, SUPPLIER_PAYMENT,
        RENT_PAYMENT, UTILITY_PAYMENT, INSURANCE_PAYMENT, MARKETING_EXPENSE, OTHER_OUTFLOW
    }

    public enum ItemStatusDto {
        PENDING, EXPECTED, COMMITTED, SETTLED, CANCELLED, FAILED
    }

    public enum RecurringFrequencyDto {
        DAILY, WEEKLY, BI_WEEKLY, MONTHLY, QUARTERLY, SEMI_ANNUALLY, ANNUALLY
    }
}
