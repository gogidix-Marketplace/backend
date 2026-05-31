package com.gogidix.finance.bankreconciliation.application.dto.response;

import com.gogidix.finance.bankreconciliation.application.dto.response.ReconciliationLineResponseDto;
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
class ReconciliationLineResponseDtoTest {

        @Test
    void testBuilder() {
        ReconciliationLineResponseDto dto = ReconciliationLineResponseDto.builder()
                        .id("test-id")
            .lineId("test-lineId")
            .reconciliationId("test-reconciliationId")
            .accountId("test-accountId")
            .lineNumber(42)
            .lineType(ReconciliationLineResponseDto.LineTypeDto.BANK_ONLY)
            .bankTransactionId("test-bankTransactionId")
            .bankTransactionDate(LocalDate.of(2025,1,15))
            .bankDescription("test-bankDescription")
            .bankReference("test-bankReference")
            .bankAmount(BigDecimal.TEN)
            .bookTransactionId("test-bookTransactionId")
            .bookTransactionDate(LocalDate.of(2025,1,15))
            .bookDescription("test-bookDescription")
            .bookReference("test-bookReference")
            .bookAmount(BigDecimal.TEN)
            .amountDifference(BigDecimal.TEN)
            .matchStatus(ReconciliationLineResponseDto.MatchStatusDto.UNMATCHED)
            .matchConfidence(null)
            .matchedBy(ReconciliationLineResponseDto.MatchedByDto.SYSTEM)
            .matchedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .discrepancyReason("test-discrepancyReason")
            .discrepancyCategory(ReconciliationLineResponseDto.DiscrepancyCategoryDto.AMOUNT_MISMATCH)
            .actionRequired(ReconciliationLineResponseDto.ActionRequiredDto.NONE)
            .actionTaken("test-actionTaken")
            .notes("test-notes")
            .currency("test-currency")
            .autoMatched(true)
            .requiresManualReview(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-lineId", dto.getLineId());
        assertEquals("test-reconciliationId", dto.getReconciliationId());
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals(42, dto.getLineNumber());
        assertEquals(ReconciliationLineResponseDto.LineTypeDto.BANK_ONLY, dto.getLineType());
        assertEquals("test-bankTransactionId", dto.getBankTransactionId());
        assertEquals(LocalDate.of(2025,1,15), dto.getBankTransactionDate());
        assertEquals("test-bankDescription", dto.getBankDescription());
        assertEquals("test-bankReference", dto.getBankReference());
        assertEquals(BigDecimal.TEN, dto.getBankAmount());
        assertEquals("test-bookTransactionId", dto.getBookTransactionId());
        assertEquals(LocalDate.of(2025,1,15), dto.getBookTransactionDate());
        assertEquals("test-bookDescription", dto.getBookDescription());
        assertEquals("test-bookReference", dto.getBookReference());
        assertEquals(BigDecimal.TEN, dto.getBookAmount());
        assertEquals(BigDecimal.TEN, dto.getAmountDifference());
        assertEquals(ReconciliationLineResponseDto.MatchStatusDto.UNMATCHED, dto.getMatchStatus());
        assertEquals(ReconciliationLineResponseDto.MatchedByDto.SYSTEM, dto.getMatchedBy());
        assertEquals("test-verifiedBy", dto.getVerifiedBy());
        assertEquals("test-discrepancyReason", dto.getDiscrepancyReason());
        assertEquals(ReconciliationLineResponseDto.DiscrepancyCategoryDto.AMOUNT_MISMATCH, dto.getDiscrepancyCategory());
        assertEquals(ReconciliationLineResponseDto.ActionRequiredDto.NONE, dto.getActionRequired());
        assertEquals("test-actionTaken", dto.getActionTaken());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-currency", dto.getCurrency());
        assertTrue(dto.getAutoMatched());
        assertTrue(dto.getRequiresManualReview());
    }

    @Test
    void testSettersAndGetters() {
        ReconciliationLineResponseDto dto = new ReconciliationLineResponseDto();
        dto.setId("val-id");
        dto.setLineId("val-lineId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setAccountId("val-accountId");
        dto.setLineNumber(99);
        dto.setLineType(ReconciliationLineResponseDto.LineTypeDto.BANK_ONLY);
        dto.setBankTransactionId("val-bankTransactionId");
        dto.setBankTransactionDate(LocalDate.of(2025,6,1));
        dto.setBankDescription("val-bankDescription");
        dto.setBankReference("val-bankReference");
        dto.setBankAmount(BigDecimal.ONE);
        dto.setBookTransactionId("val-bookTransactionId");
        dto.setBookTransactionDate(LocalDate.of(2025,6,1));
        dto.setBookDescription("val-bookDescription");
        dto.setBookReference("val-bookReference");
        dto.setBookAmount(BigDecimal.ONE);
        dto.setAmountDifference(BigDecimal.ONE);
        dto.setMatchStatus(ReconciliationLineResponseDto.MatchStatusDto.UNMATCHED);
        dto.setMatchedBy(ReconciliationLineResponseDto.MatchedByDto.SYSTEM);
        dto.setVerifiedBy("val-verifiedBy");
        dto.setDiscrepancyReason("val-discrepancyReason");
        dto.setDiscrepancyCategory(ReconciliationLineResponseDto.DiscrepancyCategoryDto.AMOUNT_MISMATCH);
        dto.setActionRequired(ReconciliationLineResponseDto.ActionRequiredDto.NONE);
        dto.setActionTaken("val-actionTaken");
        dto.setNotes("val-notes");
        dto.setCurrency("val-currency");
        dto.setAutoMatched(true);
        dto.setRequiresManualReview(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-lineId", dto.getLineId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals(99, dto.getLineNumber());
        assertEquals(ReconciliationLineResponseDto.LineTypeDto.BANK_ONLY, dto.getLineType());
        assertEquals("val-bankTransactionId", dto.getBankTransactionId());
        assertEquals(LocalDate.of(2025,6,1), dto.getBankTransactionDate());
        assertEquals("val-bankDescription", dto.getBankDescription());
        assertEquals("val-bankReference", dto.getBankReference());
        assertEquals(BigDecimal.ONE, dto.getBankAmount());
        assertEquals("val-bookTransactionId", dto.getBookTransactionId());
        assertEquals(LocalDate.of(2025,6,1), dto.getBookTransactionDate());
        assertEquals("val-bookDescription", dto.getBookDescription());
        assertEquals("val-bookReference", dto.getBookReference());
        assertEquals(BigDecimal.ONE, dto.getBookAmount());
        assertEquals(BigDecimal.ONE, dto.getAmountDifference());
        assertEquals(ReconciliationLineResponseDto.MatchStatusDto.UNMATCHED, dto.getMatchStatus());
        assertEquals(ReconciliationLineResponseDto.MatchedByDto.SYSTEM, dto.getMatchedBy());
        assertEquals("val-verifiedBy", dto.getVerifiedBy());
        assertEquals("val-discrepancyReason", dto.getDiscrepancyReason());
        assertEquals(ReconciliationLineResponseDto.DiscrepancyCategoryDto.AMOUNT_MISMATCH, dto.getDiscrepancyCategory());
        assertEquals(ReconciliationLineResponseDto.ActionRequiredDto.NONE, dto.getActionRequired());
        assertEquals("val-actionTaken", dto.getActionTaken());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-currency", dto.getCurrency());
        assertTrue(dto.getAutoMatched());
        assertTrue(dto.getRequiresManualReview());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationLineResponseDto dto1 = ReconciliationLineResponseDto.builder()
                        .id("test-id")
            .lineId("test-lineId")
            .reconciliationId("test-reconciliationId")
            .accountId("test-accountId")
            .lineNumber(42)
            .lineType(ReconciliationLineResponseDto.LineTypeDto.BANK_ONLY)
            .bankTransactionId("test-bankTransactionId")
            .bankTransactionDate(LocalDate.of(2025,1,15))
            .bankDescription("test-bankDescription")
            .bankReference("test-bankReference")
            .bankAmount(BigDecimal.TEN)
            .bookTransactionId("test-bookTransactionId")
            .bookTransactionDate(LocalDate.of(2025,1,15))
            .bookDescription("test-bookDescription")
            .bookReference("test-bookReference")
            .bookAmount(BigDecimal.TEN)
            .amountDifference(BigDecimal.TEN)
            .matchStatus(ReconciliationLineResponseDto.MatchStatusDto.UNMATCHED)
            .matchConfidence(null)
            .matchedBy(ReconciliationLineResponseDto.MatchedByDto.SYSTEM)
            .matchedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .discrepancyReason("test-discrepancyReason")
            .discrepancyCategory(ReconciliationLineResponseDto.DiscrepancyCategoryDto.AMOUNT_MISMATCH)
            .actionRequired(ReconciliationLineResponseDto.ActionRequiredDto.NONE)
            .actionTaken("test-actionTaken")
            .notes("test-notes")
            .currency("test-currency")
            .autoMatched(true)
            .requiresManualReview(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ReconciliationLineResponseDto dto2 = ReconciliationLineResponseDto.builder()
                        .id("test-id")
            .lineId("test-lineId")
            .reconciliationId("test-reconciliationId")
            .accountId("test-accountId")
            .lineNumber(42)
            .lineType(ReconciliationLineResponseDto.LineTypeDto.BANK_ONLY)
            .bankTransactionId("test-bankTransactionId")
            .bankTransactionDate(LocalDate.of(2025,1,15))
            .bankDescription("test-bankDescription")
            .bankReference("test-bankReference")
            .bankAmount(BigDecimal.TEN)
            .bookTransactionId("test-bookTransactionId")
            .bookTransactionDate(LocalDate.of(2025,1,15))
            .bookDescription("test-bookDescription")
            .bookReference("test-bookReference")
            .bookAmount(BigDecimal.TEN)
            .amountDifference(BigDecimal.TEN)
            .matchStatus(ReconciliationLineResponseDto.MatchStatusDto.UNMATCHED)
            .matchConfidence(null)
            .matchedBy(ReconciliationLineResponseDto.MatchedByDto.SYSTEM)
            .matchedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .discrepancyReason("test-discrepancyReason")
            .discrepancyCategory(ReconciliationLineResponseDto.DiscrepancyCategoryDto.AMOUNT_MISMATCH)
            .actionRequired(ReconciliationLineResponseDto.ActionRequiredDto.NONE)
            .actionTaken("test-actionTaken")
            .notes("test-notes")
            .currency("test-currency")
            .autoMatched(true)
            .requiresManualReview(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ReconciliationLineResponseDto dto = ReconciliationLineResponseDto.builder()
                        .id("test-id")
            .lineId("test-lineId")
            .reconciliationId("test-reconciliationId")
            .accountId("test-accountId")
            .lineNumber(42)
            .lineType(ReconciliationLineResponseDto.LineTypeDto.BANK_ONLY)
            .bankTransactionId("test-bankTransactionId")
            .bankTransactionDate(LocalDate.of(2025,1,15))
            .bankDescription("test-bankDescription")
            .bankReference("test-bankReference")
            .bankAmount(BigDecimal.TEN)
            .bookTransactionId("test-bookTransactionId")
            .bookTransactionDate(LocalDate.of(2025,1,15))
            .bookDescription("test-bookDescription")
            .bookReference("test-bookReference")
            .bookAmount(BigDecimal.TEN)
            .amountDifference(BigDecimal.TEN)
            .matchStatus(ReconciliationLineResponseDto.MatchStatusDto.UNMATCHED)
            .matchConfidence(null)
            .matchedBy(ReconciliationLineResponseDto.MatchedByDto.SYSTEM)
            .matchedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .discrepancyReason("test-discrepancyReason")
            .discrepancyCategory(ReconciliationLineResponseDto.DiscrepancyCategoryDto.AMOUNT_MISMATCH)
            .actionRequired(ReconciliationLineResponseDto.ActionRequiredDto.NONE)
            .actionTaken("test-actionTaken")
            .notes("test-notes")
            .currency("test-currency")
            .autoMatched(true)
            .requiresManualReview(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}