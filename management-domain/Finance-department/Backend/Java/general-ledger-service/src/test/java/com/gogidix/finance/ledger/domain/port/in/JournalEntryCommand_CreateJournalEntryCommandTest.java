package com.gogidix.finance.ledger.domain.port.in;

import com.gogidix.finance.ledger.domain.port.in.JournalEntryCommand;
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
class JournalEntryCommand_CreateJournalEntryCommandTest {

        @Test
    void testSettersAndGetters() {
        JournalEntryCommand.CreateJournalEntryCommand dto = new JournalEntryCommand.CreateJournalEntryCommand();
        dto.setTenantId("val-tenantId");
        dto.setEntryDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setCurrency("val-currency");
        dto.setCreatedBy("val-createdBy");
        dto.setCreatedByName("val-createdByName");
        dto.setReference("val-reference");
        dto.setSourceDocumentType("val-sourceDocumentType");
        dto.setSourceDocumentId("val-sourceDocumentId");
        dto.setSourceModule("val-sourceModule");
        dto.setPeriodId("val-periodId");
        dto.setFiscalYear(99);
        dto.setFiscalPeriod(99);
        dto.setRequiresApproval(true);
        dto.setNotes("val-notes");
        dto.setBatchId("val-batchId");
        dto.setExchangeRate(BigDecimal.ONE);
        dto.setBaseCurrency("val-baseCurrency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getEntryDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-createdByName", dto.getCreatedByName());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-sourceDocumentType", dto.getSourceDocumentType());
        assertEquals("val-sourceDocumentId", dto.getSourceDocumentId());
        assertEquals("val-sourceModule", dto.getSourceModule());
        assertEquals("val-periodId", dto.getPeriodId());
        assertEquals(99, dto.getFiscalYear());
        assertEquals(99, dto.getFiscalPeriod());
        assertTrue(dto.getRequiresApproval());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals(BigDecimal.ONE, dto.getExchangeRate());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryCommand.CreateJournalEntryCommand dto1 = new JournalEntryCommand.CreateJournalEntryCommand();
        JournalEntryCommand.CreateJournalEntryCommand dto2 = new JournalEntryCommand.CreateJournalEntryCommand();
        dto1.setTenantId("test");
        dto1.setEntryDate(LocalDate.of(2025,1,1));
        dto1.setDescription("test");
        dto1.setCurrency("test");
        dto1.setCreatedBy("test");
        dto1.setCreatedByName("test");
        dto1.setReference("test");
        dto1.setSourceDocumentType("test");
        dto1.setSourceDocumentId("test");
        dto1.setSourceModule("test");
        dto1.setLines(Collections.emptyList());
        dto1.setPeriodId("test");
        dto1.setFiscalYear(42);
        dto1.setFiscalPeriod(42);
        dto1.setRequiresApproval(true);
        dto1.setNotes("test");
        dto1.setBatchId("test");
        dto1.setExchangeRate(BigDecimal.TEN);
        dto1.setBaseCurrency("test");
        dto2.setTenantId("test");
        dto2.setEntryDate(LocalDate.of(2025,1,1));
        dto2.setDescription("test");
        dto2.setCurrency("test");
        dto2.setCreatedBy("test");
        dto2.setCreatedByName("test");
        dto2.setReference("test");
        dto2.setSourceDocumentType("test");
        dto2.setSourceDocumentId("test");
        dto2.setSourceModule("test");
        dto2.setLines(Collections.emptyList());
        dto2.setPeriodId("test");
        dto2.setFiscalYear(42);
        dto2.setFiscalPeriod(42);
        dto2.setRequiresApproval(true);
        dto2.setNotes("test");
        dto2.setBatchId("test");
        dto2.setExchangeRate(BigDecimal.TEN);
        dto2.setBaseCurrency("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        JournalEntryCommand.CreateJournalEntryCommand dto = new JournalEntryCommand.CreateJournalEntryCommand();
        dto.setTenantId("test");
        dto.setEntryDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setCurrency("test");
        dto.setCreatedBy("test");
        dto.setCreatedByName("test");
        dto.setReference("test");
        dto.setSourceDocumentType("test");
        dto.setSourceDocumentId("test");
        dto.setSourceModule("test");
        dto.setLines(Collections.emptyList());
        dto.setPeriodId("test");
        dto.setFiscalYear(42);
        dto.setFiscalPeriod(42);
        dto.setRequiresApproval(true);
        dto.setNotes("test");
        dto.setBatchId("test");
        dto.setExchangeRate(BigDecimal.TEN);
        dto.setBaseCurrency("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        JournalEntryCommand.CreateJournalEntryCommand dto = new JournalEntryCommand.CreateJournalEntryCommand();
        dto.setTenantId("test");
        dto.setEntryDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setCurrency("test");
        dto.setCreatedBy("test");
        dto.setCreatedByName("test");
        dto.setReference("test");
        dto.setSourceDocumentType("test");
        dto.setSourceDocumentId("test");
        dto.setSourceModule("test");
        dto.setLines(Collections.emptyList());
        dto.setPeriodId("test");
        dto.setFiscalYear(42);
        dto.setFiscalPeriod(42);
        dto.setRequiresApproval(true);
        dto.setNotes("test");
        dto.setBatchId("test");
        dto.setExchangeRate(BigDecimal.TEN);
        dto.setBaseCurrency("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}