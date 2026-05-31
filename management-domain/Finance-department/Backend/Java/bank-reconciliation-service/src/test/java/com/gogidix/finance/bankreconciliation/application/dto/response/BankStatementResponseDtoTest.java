package com.gogidix.finance.bankreconciliation.application.dto.response;

import com.gogidix.finance.bankreconciliation.application.dto.response.BankStatementResponseDto;
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
class BankStatementResponseDtoTest {

        @Test
    void testBuilder() {
        BankStatementResponseDto dto = BankStatementResponseDto.builder()
                        .id("test-id")
            .statementId("test-statementId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementDate(LocalDate.of(2025,1,15))
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .importStatus(BankStatementResponseDto.ImportStatusDto.PENDING)
            .importSource(BankStatementResponseDto.ImportSourceDto.MANUAL_UPLOAD)
            .fileReference("test-fileReference")
            .transactionCount(42)
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .importErrors(Collections.emptyList())
            .importWarnings(Collections.emptyList())
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .validatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reconciled(true)
            .reconciliationId("test-reconciliationId")
            .statementType(BankStatementResponseDto.StatementTypeDto.STATEMENT)
            .bankReference("test-bankReference")
            .transactions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-statementId", dto.getStatementId());
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals(LocalDate.of(2025,1,15), dto.getStatementDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(BigDecimal.TEN, dto.getOpeningBalance());
        assertEquals(BigDecimal.TEN, dto.getClosingBalance());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BankStatementResponseDto.ImportStatusDto.PENDING, dto.getImportStatus());
        assertEquals(BankStatementResponseDto.ImportSourceDto.MANUAL_UPLOAD, dto.getImportSource());
        assertEquals("test-fileReference", dto.getFileReference());
        assertEquals(42, dto.getTransactionCount());
        assertEquals(BigDecimal.TEN, dto.getTotalDebits());
        assertEquals(BigDecimal.TEN, dto.getTotalCredits());
        assertTrue(dto.getReconciled());
        assertEquals("test-reconciliationId", dto.getReconciliationId());
        assertEquals(BankStatementResponseDto.StatementTypeDto.STATEMENT, dto.getStatementType());
        assertEquals("test-bankReference", dto.getBankReference());
    }

    @Test
    void testSettersAndGetters() {
        BankStatementResponseDto dto = new BankStatementResponseDto();
        dto.setId("val-id");
        dto.setStatementId("val-statementId");
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setStatementDate(LocalDate.of(2025,6,1));
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setClosingBalance(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setImportStatus(BankStatementResponseDto.ImportStatusDto.PENDING);
        dto.setImportSource(BankStatementResponseDto.ImportSourceDto.MANUAL_UPLOAD);
        dto.setFileReference("val-fileReference");
        dto.setTransactionCount(99);
        dto.setTotalDebits(BigDecimal.ONE);
        dto.setTotalCredits(BigDecimal.ONE);
        dto.setReconciled(true);
        dto.setReconciliationId("val-reconciliationId");
        dto.setStatementType(BankStatementResponseDto.StatementTypeDto.STATEMENT);
        dto.setBankReference("val-bankReference");
        assertEquals("val-id", dto.getId());
        assertEquals("val-statementId", dto.getStatementId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getStatementDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals(BigDecimal.ONE, dto.getClosingBalance());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BankStatementResponseDto.ImportStatusDto.PENDING, dto.getImportStatus());
        assertEquals(BankStatementResponseDto.ImportSourceDto.MANUAL_UPLOAD, dto.getImportSource());
        assertEquals("val-fileReference", dto.getFileReference());
        assertEquals(99, dto.getTransactionCount());
        assertEquals(BigDecimal.ONE, dto.getTotalDebits());
        assertEquals(BigDecimal.ONE, dto.getTotalCredits());
        assertTrue(dto.getReconciled());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals(BankStatementResponseDto.StatementTypeDto.STATEMENT, dto.getStatementType());
        assertEquals("val-bankReference", dto.getBankReference());
    }

    @Test
    void testEqualsAndHashCode() {
        BankStatementResponseDto dto1 = BankStatementResponseDto.builder()
                        .id("test-id")
            .statementId("test-statementId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementDate(LocalDate.of(2025,1,15))
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .importStatus(BankStatementResponseDto.ImportStatusDto.PENDING)
            .importSource(BankStatementResponseDto.ImportSourceDto.MANUAL_UPLOAD)
            .fileReference("test-fileReference")
            .transactionCount(42)
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .importErrors(Collections.emptyList())
            .importWarnings(Collections.emptyList())
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .validatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reconciled(true)
            .reconciliationId("test-reconciliationId")
            .statementType(BankStatementResponseDto.StatementTypeDto.STATEMENT)
            .bankReference("test-bankReference")
            .transactions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BankStatementResponseDto dto2 = BankStatementResponseDto.builder()
                        .id("test-id")
            .statementId("test-statementId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementDate(LocalDate.of(2025,1,15))
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .importStatus(BankStatementResponseDto.ImportStatusDto.PENDING)
            .importSource(BankStatementResponseDto.ImportSourceDto.MANUAL_UPLOAD)
            .fileReference("test-fileReference")
            .transactionCount(42)
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .importErrors(Collections.emptyList())
            .importWarnings(Collections.emptyList())
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .validatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reconciled(true)
            .reconciliationId("test-reconciliationId")
            .statementType(BankStatementResponseDto.StatementTypeDto.STATEMENT)
            .bankReference("test-bankReference")
            .transactions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BankStatementResponseDto dto = BankStatementResponseDto.builder()
                        .id("test-id")
            .statementId("test-statementId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementDate(LocalDate.of(2025,1,15))
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .importStatus(BankStatementResponseDto.ImportStatusDto.PENDING)
            .importSource(BankStatementResponseDto.ImportSourceDto.MANUAL_UPLOAD)
            .fileReference("test-fileReference")
            .transactionCount(42)
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .importErrors(Collections.emptyList())
            .importWarnings(Collections.emptyList())
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .validatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reconciled(true)
            .reconciliationId("test-reconciliationId")
            .statementType(BankStatementResponseDto.StatementTypeDto.STATEMENT)
            .bankReference("test-bankReference")
            .transactions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}