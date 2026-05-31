package com.gogidix.finance.generalledger.interfaces.rest;

import com.gogidix.finance.generalledger.interfaces.rest.JournalEntryController;
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
class JournalEntryController_UpdateJournalEntryRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        JournalEntryController.UpdateJournalEntryRequestDto dto = new JournalEntryController.UpdateJournalEntryRequestDto();
        dto.setEntryDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setReference("val-reference");
        dto.setNotes("val-notes");
        assertEquals(LocalDate.of(2025,6,1), dto.getEntryDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryController.UpdateJournalEntryRequestDto dto1 = new JournalEntryController.UpdateJournalEntryRequestDto();
        JournalEntryController.UpdateJournalEntryRequestDto dto2 = new JournalEntryController.UpdateJournalEntryRequestDto();
        dto1.setEntryDate(LocalDate.of(2025,1,1));
        dto1.setDescription("test");
        dto1.setReference("test");
        dto1.setNotes("test");
        dto1.setLines(Collections.emptyList());
        dto2.setEntryDate(LocalDate.of(2025,1,1));
        dto2.setDescription("test");
        dto2.setReference("test");
        dto2.setNotes("test");
        dto2.setLines(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setEntryDate(LocalDate.of(2099,12,31));
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        JournalEntryController.UpdateJournalEntryRequestDto dto = new JournalEntryController.UpdateJournalEntryRequestDto();
        dto.setEntryDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setReference("test");
        dto.setNotes("test");
        dto.setLines(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        JournalEntryController.UpdateJournalEntryRequestDto dto = new JournalEntryController.UpdateJournalEntryRequestDto();
        dto.setEntryDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setReference("test");
        dto.setNotes("test");
        dto.setLines(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}