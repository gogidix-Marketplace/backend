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
class JournalEntryCommand_UpdateJournalEntryCommandTest {

        @Test
    void testSettersAndGetters() {
        JournalEntryCommand.UpdateJournalEntryCommand dto = new JournalEntryCommand.UpdateJournalEntryCommand();
        dto.setTenantId("val-tenantId");
        dto.setJournalEntryId("val-journalEntryId");
        dto.setEntryDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setReference("val-reference");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-journalEntryId", dto.getJournalEntryId());
        assertEquals(LocalDate.of(2025,6,1), dto.getEntryDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryCommand.UpdateJournalEntryCommand dto1 = new JournalEntryCommand.UpdateJournalEntryCommand();
        JournalEntryCommand.UpdateJournalEntryCommand dto2 = new JournalEntryCommand.UpdateJournalEntryCommand();
        dto1.setTenantId("test");
        dto1.setJournalEntryId("test");
        dto1.setEntryDate(LocalDate.of(2025,1,1));
        dto1.setDescription("test");
        dto1.setReference("test");
        dto1.setNotes("test");
        dto1.setLines(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setJournalEntryId("test");
        dto2.setEntryDate(LocalDate.of(2025,1,1));
        dto2.setDescription("test");
        dto2.setReference("test");
        dto2.setNotes("test");
        dto2.setLines(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        JournalEntryCommand.UpdateJournalEntryCommand dto = new JournalEntryCommand.UpdateJournalEntryCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
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
        JournalEntryCommand.UpdateJournalEntryCommand dto = new JournalEntryCommand.UpdateJournalEntryCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
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