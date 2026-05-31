package com.gogidix.finance.bankreconciliation.application.dto.response;

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
 * Bank Account Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountResponseDto {

    private String id;

    private String accountNumber;

    private String accountName;

    private AccountTypeDto accountType;

    private String bankName;

    private String bankCode;

    private String currency;

    private BigDecimal balance;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate balanceDate;

    private AccountStatusDto status;

    private Boolean isPrimary;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastReconciledAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastStatementDate;

    private BigDecimal openingBalance;

    private String iban;

    private String swiftCode;

    private String routingNumber;

    private String description;

    private List<String> tags;

    private StatementFrequencyDto statementFrequency;

    private BigDecimal reconciliationTolerance;

    private Boolean autoReconcile;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum AccountTypeDto {
        CHECKING, SAVINGS, MONEY_MARKET, CREDIT_CARD, LOAN, INVESTMENT, CASH, OTHER
    }

    public enum AccountStatusDto {
        ACTIVE, INACTIVE, FROZEN, CLOSED, PENDING_ACTIVATION
    }

    public enum StatementFrequencyDto {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUALLY, ON_DEMAND
    }
}
