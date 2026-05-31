package com.gogidix.finance.bankreconciliation.application.dto.response;

import com.gogidix.finance.bankreconciliation.application.dto.response.BankTransactionResponseDto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BankTransactionResponseDtoTest {

        @Test
    void testBuilder() {
        BankTransactionResponseDto dto = BankTransactionResponseDto.builder()
                        .id("test-id")
            .transactionId("test-transactionId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementId("test-statementId")
            .transactionDate(LocalDate.of(2025,1,15))
            .valueDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .bankReference("test-bankReference")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionType(BankTransactionResponseDto.TransactionTypeDto.DEBIT)
            .category("test-category")
            .subCategory("test-subCategory")
            .counterpartyName("test-counterpartyName")
            .counterpartyAccount("test-counterpartyAccount")
            .counterpartyBank("test-counterpartyBank")
            .isReconciled(true)
            .reconciliationLineId("test-reconciliationLineId")
            .reconciliationId("test-reconciliationId")
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .balanceAfter(BigDecimal.TEN)
            .runningBalance(BigDecimal.TEN)
            .isReversal(true)
            .originalTransactionId("test-originalTransactionId")
            .checkNumber("test-checkNumber")
            .paymentMethod(BankTransactionResponseDto.PaymentMethodDto.CASH)
            .status(BankTransactionResponseDto.TransactionStatusDto.PENDING)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-transactionId", dto.getTransactionId());
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-statementId", dto.getStatementId());
        assertEquals(LocalDate.of(2025,1,15), dto.getTransactionDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getValueDate());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-reference", dto.getReference());
        assertEquals("test-bankReference", dto.getBankReference());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BankTransactionResponseDto.TransactionTypeDto.DEBIT, dto.getTransactionType());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-subCategory", dto.getSubCategory());
        assertEquals("test-counterpartyName", dto.getCounterpartyName());
        assertEquals("test-counterpartyAccount", dto.getCounterpartyAccount());
        assertEquals("test-counterpartyBank", dto.getCounterpartyBank());
        assertTrue(dto.getIsReconciled());
        assertEquals("test-reconciliationLineId", dto.getReconciliationLineId());
        assertEquals("test-reconciliationId", dto.getReconciliationId());
        assertEquals(BigDecimal.TEN, dto.getBalanceAfter());
        assertEquals(BigDecimal.TEN, dto.getRunningBalance());
        assertTrue(dto.getIsReversal());
        assertEquals("test-originalTransactionId", dto.getOriginalTransactionId());
        assertEquals("test-checkNumber", dto.getCheckNumber());
        assertEquals(BankTransactionResponseDto.PaymentMethodDto.CASH, dto.getPaymentMethod());
        assertEquals(BankTransactionResponseDto.TransactionStatusDto.PENDING, dto.getStatus());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        BankTransactionResponseDto dto = new BankTransactionResponseDto();
        dto.setId("val-id");
        dto.setTransactionId("val-transactionId");
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setStatementId("val-statementId");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setValueDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setReference("val-reference");
        dto.setBankReference("val-bankReference");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setTransactionType(BankTransactionResponseDto.TransactionTypeDto.DEBIT);
        dto.setCategory("val-category");
        dto.setSubCategory("val-subCategory");
        dto.setCounterpartyName("val-counterpartyName");
        dto.setCounterpartyAccount("val-counterpartyAccount");
        dto.setCounterpartyBank("val-counterpartyBank");
        dto.setIsReconciled(true);
        dto.setReconciliationLineId("val-reconciliationLineId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setBalanceAfter(BigDecimal.ONE);
        dto.setRunningBalance(BigDecimal.ONE);
        dto.setIsReversal(true);
        dto.setOriginalTransactionId("val-originalTransactionId");
        dto.setCheckNumber("val-checkNumber");
        dto.setPaymentMethod(BankTransactionResponseDto.PaymentMethodDto.CASH);
        dto.setStatus(BankTransactionResponseDto.TransactionStatusDto.PENDING);
        dto.setNotes("val-notes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-statementId", dto.getStatementId());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getValueDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-bankReference", dto.getBankReference());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BankTransactionResponseDto.TransactionTypeDto.DEBIT, dto.getTransactionType());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-subCategory", dto.getSubCategory());
        assertEquals("val-counterpartyName", dto.getCounterpartyName());
        assertEquals("val-counterpartyAccount", dto.getCounterpartyAccount());
        assertEquals("val-counterpartyBank", dto.getCounterpartyBank());
        assertTrue(dto.getIsReconciled());
        assertEquals("val-reconciliationLineId", dto.getReconciliationLineId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals(BigDecimal.ONE, dto.getBalanceAfter());
        assertEquals(BigDecimal.ONE, dto.getRunningBalance());
        assertTrue(dto.getIsReversal());
        assertEquals("val-originalTransactionId", dto.getOriginalTransactionId());
        assertEquals("val-checkNumber", dto.getCheckNumber());
        assertEquals(BankTransactionResponseDto.PaymentMethodDto.CASH, dto.getPaymentMethod());
        assertEquals(BankTransactionResponseDto.TransactionStatusDto.PENDING, dto.getStatus());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        BankTransactionResponseDto dto1 = BankTransactionResponseDto.builder()
                        .id("test-id")
            .transactionId("test-transactionId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementId("test-statementId")
            .transactionDate(LocalDate.of(2025,1,15))
            .valueDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .bankReference("test-bankReference")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionType(BankTransactionResponseDto.TransactionTypeDto.DEBIT)
            .category("test-category")
            .subCategory("test-subCategory")
            .counterpartyName("test-counterpartyName")
            .counterpartyAccount("test-counterpartyAccount")
            .counterpartyBank("test-counterpartyBank")
            .isReconciled(true)
            .reconciliationLineId("test-reconciliationLineId")
            .reconciliationId("test-reconciliationId")
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .balanceAfter(BigDecimal.TEN)
            .runningBalance(BigDecimal.TEN)
            .isReversal(true)
            .originalTransactionId("test-originalTransactionId")
            .checkNumber("test-checkNumber")
            .paymentMethod(BankTransactionResponseDto.PaymentMethodDto.CASH)
            .status(BankTransactionResponseDto.TransactionStatusDto.PENDING)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BankTransactionResponseDto dto2 = BankTransactionResponseDto.builder()
                        .id("test-id")
            .transactionId("test-transactionId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementId("test-statementId")
            .transactionDate(LocalDate.of(2025,1,15))
            .valueDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .bankReference("test-bankReference")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionType(BankTransactionResponseDto.TransactionTypeDto.DEBIT)
            .category("test-category")
            .subCategory("test-subCategory")
            .counterpartyName("test-counterpartyName")
            .counterpartyAccount("test-counterpartyAccount")
            .counterpartyBank("test-counterpartyBank")
            .isReconciled(true)
            .reconciliationLineId("test-reconciliationLineId")
            .reconciliationId("test-reconciliationId")
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .balanceAfter(BigDecimal.TEN)
            .runningBalance(BigDecimal.TEN)
            .isReversal(true)
            .originalTransactionId("test-originalTransactionId")
            .checkNumber("test-checkNumber")
            .paymentMethod(BankTransactionResponseDto.PaymentMethodDto.CASH)
            .status(BankTransactionResponseDto.TransactionStatusDto.PENDING)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BankTransactionResponseDto dto = BankTransactionResponseDto.builder()
                        .id("test-id")
            .transactionId("test-transactionId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementId("test-statementId")
            .transactionDate(LocalDate.of(2025,1,15))
            .valueDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .bankReference("test-bankReference")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionType(BankTransactionResponseDto.TransactionTypeDto.DEBIT)
            .category("test-category")
            .subCategory("test-subCategory")
            .counterpartyName("test-counterpartyName")
            .counterpartyAccount("test-counterpartyAccount")
            .counterpartyBank("test-counterpartyBank")
            .isReconciled(true)
            .reconciliationLineId("test-reconciliationLineId")
            .reconciliationId("test-reconciliationId")
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .balanceAfter(BigDecimal.TEN)
            .runningBalance(BigDecimal.TEN)
            .isReversal(true)
            .originalTransactionId("test-originalTransactionId")
            .checkNumber("test-checkNumber")
            .paymentMethod(BankTransactionResponseDto.PaymentMethodDto.CASH)
            .status(BankTransactionResponseDto.TransactionStatusDto.PENDING)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}