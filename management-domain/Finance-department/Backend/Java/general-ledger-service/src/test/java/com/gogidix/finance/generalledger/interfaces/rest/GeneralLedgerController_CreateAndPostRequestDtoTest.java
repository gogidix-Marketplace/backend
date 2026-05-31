package com.gogidix.finance.generalledger.interfaces.rest;

import com.gogidix.finance.generalledger.interfaces.rest.GeneralLedgerController;
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
class GeneralLedgerController_CreateAndPostRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        GeneralLedgerController.CreateAndPostRequestDto dto = new GeneralLedgerController.CreateAndPostRequestDto();
        dto.setEntryDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setCurrency("val-currency");
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
        assertEquals(LocalDate.of(2025,6,1), dto.getEntryDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-currency", dto.getCurrency());
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
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralLedgerController.CreateAndPostRequestDto dto1 = new GeneralLedgerController.CreateAndPostRequestDto();
        GeneralLedgerController.CreateAndPostRequestDto dto2 = new GeneralLedgerController.CreateAndPostRequestDto();
        dto1.setEntryDate(LocalDate.of(2025,1,1));
        dto1.setDescription("test");
        dto1.setCurrency("test");
        dto1.setCreatedByName("test");
        dto1.setReference("test");
        dto1.setSourceDocumentType("test");
        dto1.setSourceDocumentId("test");
        dto1.setSourceModule("test");
        dto1.setPeriodId("test");
        dto1.setFiscalYear(42);
        dto1.setFiscalPeriod(42);
        dto1.setRequiresApproval(true);
        dto1.setNotes("test");
        dto1.setLines(Collections.emptyList());
        dto2.setEntryDate(LocalDate.of(2025,1,1));
        dto2.setDescription("test");
        dto2.setCurrency("test");
        dto2.setCreatedByName("test");
        dto2.setReference("test");
        dto2.setSourceDocumentType("test");
        dto2.setSourceDocumentId("test");
        dto2.setSourceModule("test");
        dto2.setPeriodId("test");
        dto2.setFiscalYear(42);
        dto2.setFiscalPeriod(42);
        dto2.setRequiresApproval(true);
        dto2.setNotes("test");
        dto2.setLines(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setEntryDate(LocalDate.of(2099,12,31));
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        GeneralLedgerController.CreateAndPostRequestDto dto = new GeneralLedgerController.CreateAndPostRequestDto();
        dto.setEntryDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setCurrency("test");
        dto.setCreatedByName("test");
        dto.setReference("test");
        dto.setSourceDocumentType("test");
        dto.setSourceDocumentId("test");
        dto.setSourceModule("test");
        dto.setPeriodId("test");
        dto.setFiscalYear(42);
        dto.setFiscalPeriod(42);
        dto.setRequiresApproval(true);
        dto.setNotes("test");
        dto.setLines(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        GeneralLedgerController.CreateAndPostRequestDto dto = new GeneralLedgerController.CreateAndPostRequestDto();
        dto.setEntryDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setCurrency("test");
        dto.setCreatedByName("test");
        dto.setReference("test");
        dto.setSourceDocumentType("test");
        dto.setSourceDocumentId("test");
        dto.setSourceModule("test");
        dto.setPeriodId("test");
        dto.setFiscalYear(42);
        dto.setFiscalPeriod(42);
        dto.setRequiresApproval(true);
        dto.setNotes("test");
        dto.setLines(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}