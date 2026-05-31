package com.gogidix.finance.bankreconciliation.application.dto.response;

import com.gogidix.finance.bankreconciliation.application.dto.response.ReconciliationResponseDto;
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
class ReconciliationResponseDtoTest {

        @Test
    void testBuilder() {
        ReconciliationResponseDto dto = ReconciliationResponseDto.builder()
                        .id("test-id")
            .reconciliationId("test-reconciliationId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementId("test-statementId")
            .reconciliationDate(LocalDate.of(2025,1,15))
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .status(ReconciliationResponseDto.ReconciliationStatusDto.PENDING)
            .startingBalance(BigDecimal.TEN)
            .endingBalance(BigDecimal.TEN)
            .bookBalance(BigDecimal.TEN)
            .bankBalance(BigDecimal.TEN)
            .difference(BigDecimal.TEN)
            .tolerance(BigDecimal.TEN)
            .isBalanced(true)
            .reconciledBy("test-reconciledBy")
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lineCount(42)
            .matchedCount(42)
            .unmatchedCount(42)
            .discrepancyCount(42)
            .notes("test-notes")
            .autoReconciled(true)
            .reconciliationMethod(ReconciliationResponseDto.ReconciliationMethodDto.AUTOMATIC)
            .completionPercentage(42)
            .errorMessage("test-errorMessage")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-reconciliationId", dto.getReconciliationId());
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-statementId", dto.getStatementId());
        assertEquals(LocalDate.of(2025,1,15), dto.getReconciliationDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodEnd());
        assertEquals(ReconciliationResponseDto.ReconciliationStatusDto.PENDING, dto.getStatus());
        assertEquals(BigDecimal.TEN, dto.getStartingBalance());
        assertEquals(BigDecimal.TEN, dto.getEndingBalance());
        assertEquals(BigDecimal.TEN, dto.getBookBalance());
        assertEquals(BigDecimal.TEN, dto.getBankBalance());
        assertEquals(BigDecimal.TEN, dto.getDifference());
        assertEquals(BigDecimal.TEN, dto.getTolerance());
        assertTrue(dto.getIsBalanced());
        assertEquals("test-reconciledBy", dto.getReconciledBy());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals(42, dto.getLineCount());
        assertEquals(42, dto.getMatchedCount());
        assertEquals(42, dto.getUnmatchedCount());
        assertEquals(42, dto.getDiscrepancyCount());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getAutoReconciled());
        assertEquals(ReconciliationResponseDto.ReconciliationMethodDto.AUTOMATIC, dto.getReconciliationMethod());
        assertEquals(42, dto.getCompletionPercentage());
        assertEquals("test-errorMessage", dto.getErrorMessage());
    }

    @Test
    void testSettersAndGetters() {
        ReconciliationResponseDto dto = new ReconciliationResponseDto();
        dto.setId("val-id");
        dto.setReconciliationId("val-reconciliationId");
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setStatementId("val-statementId");
        dto.setReconciliationDate(LocalDate.of(2025,6,1));
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setStatus(ReconciliationResponseDto.ReconciliationStatusDto.PENDING);
        dto.setStartingBalance(BigDecimal.ONE);
        dto.setEndingBalance(BigDecimal.ONE);
        dto.setBookBalance(BigDecimal.ONE);
        dto.setBankBalance(BigDecimal.ONE);
        dto.setDifference(BigDecimal.ONE);
        dto.setTolerance(BigDecimal.ONE);
        dto.setIsBalanced(true);
        dto.setReconciledBy("val-reconciledBy");
        dto.setApprovedBy("val-approvedBy");
        dto.setLineCount(99);
        dto.setMatchedCount(99);
        dto.setUnmatchedCount(99);
        dto.setDiscrepancyCount(99);
        dto.setNotes("val-notes");
        dto.setAutoReconciled(true);
        dto.setReconciliationMethod(ReconciliationResponseDto.ReconciliationMethodDto.AUTOMATIC);
        dto.setCompletionPercentage(99);
        dto.setErrorMessage("val-errorMessage");
        assertEquals("val-id", dto.getId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-statementId", dto.getStatementId());
        assertEquals(LocalDate.of(2025,6,1), dto.getReconciliationDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals(ReconciliationResponseDto.ReconciliationStatusDto.PENDING, dto.getStatus());
        assertEquals(BigDecimal.ONE, dto.getStartingBalance());
        assertEquals(BigDecimal.ONE, dto.getEndingBalance());
        assertEquals(BigDecimal.ONE, dto.getBookBalance());
        assertEquals(BigDecimal.ONE, dto.getBankBalance());
        assertEquals(BigDecimal.ONE, dto.getDifference());
        assertEquals(BigDecimal.ONE, dto.getTolerance());
        assertTrue(dto.getIsBalanced());
        assertEquals("val-reconciledBy", dto.getReconciledBy());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals(99, dto.getLineCount());
        assertEquals(99, dto.getMatchedCount());
        assertEquals(99, dto.getUnmatchedCount());
        assertEquals(99, dto.getDiscrepancyCount());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getAutoReconciled());
        assertEquals(ReconciliationResponseDto.ReconciliationMethodDto.AUTOMATIC, dto.getReconciliationMethod());
        assertEquals(99, dto.getCompletionPercentage());
        assertEquals("val-errorMessage", dto.getErrorMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationResponseDto dto1 = ReconciliationResponseDto.builder()
                        .id("test-id")
            .reconciliationId("test-reconciliationId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementId("test-statementId")
            .reconciliationDate(LocalDate.of(2025,1,15))
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .status(ReconciliationResponseDto.ReconciliationStatusDto.PENDING)
            .startingBalance(BigDecimal.TEN)
            .endingBalance(BigDecimal.TEN)
            .bookBalance(BigDecimal.TEN)
            .bankBalance(BigDecimal.TEN)
            .difference(BigDecimal.TEN)
            .tolerance(BigDecimal.TEN)
            .isBalanced(true)
            .reconciledBy("test-reconciledBy")
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lineCount(42)
            .matchedCount(42)
            .unmatchedCount(42)
            .discrepancyCount(42)
            .notes("test-notes")
            .autoReconciled(true)
            .reconciliationMethod(ReconciliationResponseDto.ReconciliationMethodDto.AUTOMATIC)
            .completionPercentage(42)
            .errorMessage("test-errorMessage")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ReconciliationResponseDto dto2 = ReconciliationResponseDto.builder()
                        .id("test-id")
            .reconciliationId("test-reconciliationId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementId("test-statementId")
            .reconciliationDate(LocalDate.of(2025,1,15))
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .status(ReconciliationResponseDto.ReconciliationStatusDto.PENDING)
            .startingBalance(BigDecimal.TEN)
            .endingBalance(BigDecimal.TEN)
            .bookBalance(BigDecimal.TEN)
            .bankBalance(BigDecimal.TEN)
            .difference(BigDecimal.TEN)
            .tolerance(BigDecimal.TEN)
            .isBalanced(true)
            .reconciledBy("test-reconciledBy")
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lineCount(42)
            .matchedCount(42)
            .unmatchedCount(42)
            .discrepancyCount(42)
            .notes("test-notes")
            .autoReconciled(true)
            .reconciliationMethod(ReconciliationResponseDto.ReconciliationMethodDto.AUTOMATIC)
            .completionPercentage(42)
            .errorMessage("test-errorMessage")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ReconciliationResponseDto dto = ReconciliationResponseDto.builder()
                        .id("test-id")
            .reconciliationId("test-reconciliationId")
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .statementId("test-statementId")
            .reconciliationDate(LocalDate.of(2025,1,15))
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .status(ReconciliationResponseDto.ReconciliationStatusDto.PENDING)
            .startingBalance(BigDecimal.TEN)
            .endingBalance(BigDecimal.TEN)
            .bookBalance(BigDecimal.TEN)
            .bankBalance(BigDecimal.TEN)
            .difference(BigDecimal.TEN)
            .tolerance(BigDecimal.TEN)
            .isBalanced(true)
            .reconciledBy("test-reconciledBy")
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lineCount(42)
            .matchedCount(42)
            .unmatchedCount(42)
            .discrepancyCount(42)
            .notes("test-notes")
            .autoReconciled(true)
            .reconciliationMethod(ReconciliationResponseDto.ReconciliationMethodDto.AUTOMATIC)
            .completionPercentage(42)
            .errorMessage("test-errorMessage")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}