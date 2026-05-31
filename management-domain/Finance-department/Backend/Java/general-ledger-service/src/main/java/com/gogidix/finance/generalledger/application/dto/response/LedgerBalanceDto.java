package com.gogidix.finance.generalledger.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Ledger Balance Response DTO
 * Represents the balance information for a ledger account
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerBalanceDto {

    private String accountId;

    private String accountNumber;

    private String accountName;

    private LedgerAccountResponseDto.AccountTypeDto accountType;

    private BigDecimal currentBalance;

    private BigDecimal debitBalance;

    private BigDecimal creditBalance;

    private BigDecimal openingBalance;

    private String currency;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate asOfDate;

    private Integer normalBalanceSide;

    private BigDecimal netChange;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodEndDate;

    private Integer transactionCount;

    private BigDecimal lastTransactionAmount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private java.time.Instant lastTransactionDate;

    private Boolean isReconciled;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private java.time.Instant lastReconciledAt;

    private String balanceStatus; // POSITIVE, NEGATIVE, ZERO
}
