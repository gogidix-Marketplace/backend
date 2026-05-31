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
import java.util.Map;

/**
 * Bank Transaction Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankTransactionResponseDto {

    private String id;

    private String transactionId;

    private String accountId;

    private String accountNumber;

    private String statementId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate valueDate;

    private String description;

    private String reference;

    private String bankReference;

    private BigDecimal amount;

    private String currency;

    private TransactionTypeDto transactionType;

    private String category;

    private String subCategory;

    private String counterpartyName;

    private String counterpartyAccount;

    private String counterpartyBank;

    private Boolean isReconciled;

    private String reconciliationLineId;

    private String reconciliationId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant reconciledAt;

    private BigDecimal balanceAfter;

    private BigDecimal runningBalance;

    private Boolean isReversal;

    private String originalTransactionId;

    private String checkNumber;

    private PaymentMethodDto paymentMethod;

    private TransactionStatusDto status;

    private String notes;

    private List<String> tags;

    private Map<String, Object> metadata;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum TransactionTypeDto {
        DEBIT, CREDIT, TRANSFER_IN, TRANSFER_OUT, DIRECT_DEBIT, DIRECT_CREDIT,
        STANDING_ORDER, WIRE_TRANSFER, CHECK, INTEREST, FEE, TAX, REFUND,
        CHARGEBACK, ADJUSTMENT, OTHER
    }

    public enum PaymentMethodDto {
        CASH, CHECK, WIRE, ACH, CARD, ELECTRONIC, OTHER
    }

    public enum TransactionStatusDto {
        PENDING, POSTED, CLEARED, REVERSED, DISPUTED, HOLD
    }
}
