package com.gogidix.finance.generalledger.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Trial Balance Response DTO
 * Represents the trial balance report for validation of double-entry accounting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialBalanceDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate asOfDate;

    private String currency;

    private BigDecimal totalDebits;

    private BigDecimal totalCredits;

    private Boolean isBalanced;

    private BigDecimal difference;

    private List<AccountBalanceDetailDto> accountDetails;

    private Integer fiscalYear;

    private Integer fiscalPeriod;

    private String periodName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private java.time.Instant generatedAt;

    private Integer totalAccounts;

    private Integer activeAccounts;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBalanceDetailDto {
        private String accountId;
        private String accountNumber;
        private String accountName;
        private LedgerAccountResponseDto.AccountTypeDto accountType;
        private LedgerAccountResponseDto.AccountSubTypeDto accountSubType;
        private BigDecimal debitBalance;
        private BigDecimal creditBalance;
        private BigDecimal netBalance;
        private String currency;
        private LedgerAccountResponseDto.AccountStatusDto status;
    }
}
