package com.gogidix.finance.generalledger.application.dto.response;

import com.gogidix.finance.generalledger.application.dto.response.JournalEntryResponseDto;
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
class JournalEntryResponseDtoTest {

        @Test
    void testBuilder() {
        JournalEntryResponseDto dto = JournalEntryResponseDto.builder()
                        .id("test-id")
            .journalEntryId("test-journalEntryId")
            .tenantId("test-tenantId")
            .entryNumber("test-entryNumber")
            .entryDate(LocalDate.of(2025,1,15))
            .postingDate(Instant.parse("2025-01-15T10:00:00Z"))
            .status(JournalEntryResponseDto.JournalEntryStatusDto.DRAFT)
            .description("test-description")
            .reference("test-reference")
            .sourceDocumentType("test-sourceDocumentType")
            .sourceDocumentId("test-sourceDocumentId")
            .sourceModule("test-sourceModule")
            .periodId("test-periodId")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .createdByUserId("test-createdByUserId")
            .createdByName("test-createdByName")
            .approvedByUserId("test-approvedByUserId")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .postedByUserId("test-postedByUserId")
            .postedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalDebit(BigDecimal.TEN)
            .totalCredit(BigDecimal.TEN)
            .currency("test-currency")
            .exchangeRate(BigDecimal.TEN)
            .baseCurrency("test-baseCurrency")
            .isReversed(true)
            .reversedByEntryId("test-reversedByEntryId")
            .reversalDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lines(Collections.emptyList())
            .attachmentUrls(Collections.emptyList())
            .notes("test-notes")
            .batchId("test-batchId")
            .recurrenceId("test-recurrenceId")
            .isRecurring(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-journalEntryId", dto.getJournalEntryId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-entryNumber", dto.getEntryNumber());
        assertEquals(LocalDate.of(2025,1,15), dto.getEntryDate());
        assertEquals(JournalEntryResponseDto.JournalEntryStatusDto.DRAFT, dto.getStatus());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-reference", dto.getReference());
        assertEquals("test-sourceDocumentType", dto.getSourceDocumentType());
        assertEquals("test-sourceDocumentId", dto.getSourceDocumentId());
        assertEquals("test-sourceModule", dto.getSourceModule());
        assertEquals("test-periodId", dto.getPeriodId());
        assertEquals(42, dto.getFiscalYear());
        assertEquals(42, dto.getFiscalPeriod());
        assertEquals("test-createdByUserId", dto.getCreatedByUserId());
        assertEquals("test-createdByName", dto.getCreatedByName());
        assertEquals("test-approvedByUserId", dto.getApprovedByUserId());
        assertEquals("test-postedByUserId", dto.getPostedByUserId());
        assertEquals(BigDecimal.TEN, dto.getTotalDebit());
        assertEquals(BigDecimal.TEN, dto.getTotalCredit());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getExchangeRate());
        assertEquals("test-baseCurrency", dto.getBaseCurrency());
        assertTrue(dto.getIsReversed());
        assertEquals("test-reversedByEntryId", dto.getReversedByEntryId());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-batchId", dto.getBatchId());
        assertEquals("test-recurrenceId", dto.getRecurrenceId());
        assertTrue(dto.getIsRecurring());
    }

    @Test
    void testSettersAndGetters() {
        JournalEntryResponseDto dto = new JournalEntryResponseDto();
        dto.setId("val-id");
        dto.setJournalEntryId("val-journalEntryId");
        dto.setTenantId("val-tenantId");
        dto.setEntryNumber("val-entryNumber");
        dto.setEntryDate(LocalDate.of(2025,6,1));
        dto.setStatus(JournalEntryResponseDto.JournalEntryStatusDto.DRAFT);
        dto.setDescription("val-description");
        dto.setReference("val-reference");
        dto.setSourceDocumentType("val-sourceDocumentType");
        dto.setSourceDocumentId("val-sourceDocumentId");
        dto.setSourceModule("val-sourceModule");
        dto.setPeriodId("val-periodId");
        dto.setFiscalYear(99);
        dto.setFiscalPeriod(99);
        dto.setCreatedByUserId("val-createdByUserId");
        dto.setCreatedByName("val-createdByName");
        dto.setApprovedByUserId("val-approvedByUserId");
        dto.setPostedByUserId("val-postedByUserId");
        dto.setTotalDebit(BigDecimal.ONE);
        dto.setTotalCredit(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setExchangeRate(BigDecimal.ONE);
        dto.setBaseCurrency("val-baseCurrency");
        dto.setIsReversed(true);
        dto.setReversedByEntryId("val-reversedByEntryId");
        dto.setNotes("val-notes");
        dto.setBatchId("val-batchId");
        dto.setRecurrenceId("val-recurrenceId");
        dto.setIsRecurring(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-journalEntryId", dto.getJournalEntryId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-entryNumber", dto.getEntryNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getEntryDate());
        assertEquals(JournalEntryResponseDto.JournalEntryStatusDto.DRAFT, dto.getStatus());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-sourceDocumentType", dto.getSourceDocumentType());
        assertEquals("val-sourceDocumentId", dto.getSourceDocumentId());
        assertEquals("val-sourceModule", dto.getSourceModule());
        assertEquals("val-periodId", dto.getPeriodId());
        assertEquals(99, dto.getFiscalYear());
        assertEquals(99, dto.getFiscalPeriod());
        assertEquals("val-createdByUserId", dto.getCreatedByUserId());
        assertEquals("val-createdByName", dto.getCreatedByName());
        assertEquals("val-approvedByUserId", dto.getApprovedByUserId());
        assertEquals("val-postedByUserId", dto.getPostedByUserId());
        assertEquals(BigDecimal.ONE, dto.getTotalDebit());
        assertEquals(BigDecimal.ONE, dto.getTotalCredit());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getExchangeRate());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertTrue(dto.getIsReversed());
        assertEquals("val-reversedByEntryId", dto.getReversedByEntryId());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals("val-recurrenceId", dto.getRecurrenceId());
        assertTrue(dto.getIsRecurring());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryResponseDto dto1 = JournalEntryResponseDto.builder()
                        .id("test-id")
            .journalEntryId("test-journalEntryId")
            .tenantId("test-tenantId")
            .entryNumber("test-entryNumber")
            .entryDate(LocalDate.of(2025,1,15))
            .postingDate(Instant.parse("2025-01-15T10:00:00Z"))
            .status(JournalEntryResponseDto.JournalEntryStatusDto.DRAFT)
            .description("test-description")
            .reference("test-reference")
            .sourceDocumentType("test-sourceDocumentType")
            .sourceDocumentId("test-sourceDocumentId")
            .sourceModule("test-sourceModule")
            .periodId("test-periodId")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .createdByUserId("test-createdByUserId")
            .createdByName("test-createdByName")
            .approvedByUserId("test-approvedByUserId")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .postedByUserId("test-postedByUserId")
            .postedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalDebit(BigDecimal.TEN)
            .totalCredit(BigDecimal.TEN)
            .currency("test-currency")
            .exchangeRate(BigDecimal.TEN)
            .baseCurrency("test-baseCurrency")
            .isReversed(true)
            .reversedByEntryId("test-reversedByEntryId")
            .reversalDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lines(Collections.emptyList())
            .attachmentUrls(Collections.emptyList())
            .notes("test-notes")
            .batchId("test-batchId")
            .recurrenceId("test-recurrenceId")
            .isRecurring(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        JournalEntryResponseDto dto2 = JournalEntryResponseDto.builder()
                        .id("test-id")
            .journalEntryId("test-journalEntryId")
            .tenantId("test-tenantId")
            .entryNumber("test-entryNumber")
            .entryDate(LocalDate.of(2025,1,15))
            .postingDate(Instant.parse("2025-01-15T10:00:00Z"))
            .status(JournalEntryResponseDto.JournalEntryStatusDto.DRAFT)
            .description("test-description")
            .reference("test-reference")
            .sourceDocumentType("test-sourceDocumentType")
            .sourceDocumentId("test-sourceDocumentId")
            .sourceModule("test-sourceModule")
            .periodId("test-periodId")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .createdByUserId("test-createdByUserId")
            .createdByName("test-createdByName")
            .approvedByUserId("test-approvedByUserId")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .postedByUserId("test-postedByUserId")
            .postedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalDebit(BigDecimal.TEN)
            .totalCredit(BigDecimal.TEN)
            .currency("test-currency")
            .exchangeRate(BigDecimal.TEN)
            .baseCurrency("test-baseCurrency")
            .isReversed(true)
            .reversedByEntryId("test-reversedByEntryId")
            .reversalDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lines(Collections.emptyList())
            .attachmentUrls(Collections.emptyList())
            .notes("test-notes")
            .batchId("test-batchId")
            .recurrenceId("test-recurrenceId")
            .isRecurring(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        JournalEntryResponseDto dto = JournalEntryResponseDto.builder()
                        .id("test-id")
            .journalEntryId("test-journalEntryId")
            .tenantId("test-tenantId")
            .entryNumber("test-entryNumber")
            .entryDate(LocalDate.of(2025,1,15))
            .postingDate(Instant.parse("2025-01-15T10:00:00Z"))
            .status(JournalEntryResponseDto.JournalEntryStatusDto.DRAFT)
            .description("test-description")
            .reference("test-reference")
            .sourceDocumentType("test-sourceDocumentType")
            .sourceDocumentId("test-sourceDocumentId")
            .sourceModule("test-sourceModule")
            .periodId("test-periodId")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .createdByUserId("test-createdByUserId")
            .createdByName("test-createdByName")
            .approvedByUserId("test-approvedByUserId")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .postedByUserId("test-postedByUserId")
            .postedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalDebit(BigDecimal.TEN)
            .totalCredit(BigDecimal.TEN)
            .currency("test-currency")
            .exchangeRate(BigDecimal.TEN)
            .baseCurrency("test-baseCurrency")
            .isReversed(true)
            .reversedByEntryId("test-reversedByEntryId")
            .reversalDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lines(Collections.emptyList())
            .attachmentUrls(Collections.emptyList())
            .notes("test-notes")
            .batchId("test-batchId")
            .recurrenceId("test-recurrenceId")
            .isRecurring(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}