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
 * Ledger Account Response DTO
 * Represents the response structure for ledger account operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerAccountResponseDto {

    private String id;

    private String accountId;

    private String tenantId;

    private String accountNumber;

    private String accountName;

    private AccountTypeDto accountType;

    private AccountSubTypeDto accountSubType;

    private String parentAccountId;

    private Integer accountLevel;

    private AccountStatusDto status;

    private String currency;

    private BigDecimal currentBalance;

    private BigDecimal debitBalance;

    private BigDecimal creditBalance;

    private BigDecimal openingBalance;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openingBalanceDate;

    private String description;

    private String costCenter;

    private String department;

    private String location;

    private Boolean isCashAccount;

    private Boolean isReconcilable;

    private Boolean isTaxAccount;

    private String taxCode;

    private Boolean allowsManualEntry;

    private Integer normalBalanceSide;

    private String createdByUserId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastReconciledAt;

    private String lastReconciledBy;

    private List<AccountTagDto> tags;

    private BigDecimal creditLimit;

    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant archivedAt;

    private String archivedBy;

    private String archivedReason;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountTagDto {
        private String key;
        private String value;
    }

    public enum AccountTypeDto {
        ASSET,
        LIABILITY,
        EQUITY,
        REVENUE,
        EXPENSE
    }

    public enum AccountSubTypeDto {
        CURRENT_ASSET,
        FIXED_ASSET,
        INTANGIBLE_ASSET,
        NON_CURRENT_ASSET,
        CURRENT_LIABILITY,
        LONG_TERM_LIABILITY,
        PROVISION,
        SHARE_CAPITAL,
        RETAINED_EARNINGS,
        RESERVES,
        OTHER_EQUITY,
        OPERATING_REVENUE,
        NON_OPERATING_REVENUE,
        OTHER_INCOME,
        COST_OF_SALES,
        OPERATING_EXPENSE,
        NON_OPERATING_EXPENSE,
        DEPRECIATION,
        AMORTIZATION
    }

    public enum AccountStatusDto {
        ACTIVE,
        INACTIVE,
        PENDING_APPROVAL,
        ARCHIVED,
        FROZEN
    }
}
