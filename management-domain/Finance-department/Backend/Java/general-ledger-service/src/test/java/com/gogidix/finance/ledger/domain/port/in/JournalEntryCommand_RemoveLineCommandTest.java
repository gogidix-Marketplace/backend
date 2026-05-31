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
class JournalEntryCommand_RemoveLineCommandTest {

        @Test
    void testSettersAndGetters() {
        JournalEntryCommand.RemoveLineCommand dto = new JournalEntryCommand.RemoveLineCommand();
        dto.setTenantId("val-tenantId");
        dto.setJournalEntryId("val-journalEntryId");
        dto.setLineId("val-lineId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-journalEntryId", dto.getJournalEntryId());
        assertEquals("val-lineId", dto.getLineId());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryCommand.RemoveLineCommand dto1 = new JournalEntryCommand.RemoveLineCommand();
        JournalEntryCommand.RemoveLineCommand dto2 = new JournalEntryCommand.RemoveLineCommand();
        dto1.setTenantId("test");
        dto1.setJournalEntryId("test");
        dto1.setLineId("test");
        dto2.setTenantId("test");
        dto2.setJournalEntryId("test");
        dto2.setLineId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        JournalEntryCommand.RemoveLineCommand dto = new JournalEntryCommand.RemoveLineCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
        dto.setLineId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        JournalEntryCommand.RemoveLineCommand dto = new JournalEntryCommand.RemoveLineCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
        dto.setLineId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}