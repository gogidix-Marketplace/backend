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
class JournalEntryCommand_ReverseJournalEntryCommandTest {

        @Test
    void testSettersAndGetters() {
        JournalEntryCommand.ReverseJournalEntryCommand dto = new JournalEntryCommand.ReverseJournalEntryCommand();
        dto.setTenantId("val-tenantId");
        dto.setJournalEntryId("val-journalEntryId");
        dto.setReversalReason("val-reversalReason");
        dto.setReversedBy("val-reversedBy");
        dto.setReversalDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-journalEntryId", dto.getJournalEntryId());
        assertEquals("val-reversalReason", dto.getReversalReason());
        assertEquals("val-reversedBy", dto.getReversedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getReversalDate());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryCommand.ReverseJournalEntryCommand dto1 = new JournalEntryCommand.ReverseJournalEntryCommand();
        JournalEntryCommand.ReverseJournalEntryCommand dto2 = new JournalEntryCommand.ReverseJournalEntryCommand();
        dto1.setTenantId("test");
        dto1.setJournalEntryId("test");
        dto1.setReversalReason("test");
        dto1.setReversedBy("test");
        dto1.setReversalDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setJournalEntryId("test");
        dto2.setReversalReason("test");
        dto2.setReversedBy("test");
        dto2.setReversalDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        JournalEntryCommand.ReverseJournalEntryCommand dto = new JournalEntryCommand.ReverseJournalEntryCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
        dto.setReversalReason("test");
        dto.setReversedBy("test");
        dto.setReversalDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        JournalEntryCommand.ReverseJournalEntryCommand dto = new JournalEntryCommand.ReverseJournalEntryCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
        dto.setReversalReason("test");
        dto.setReversedBy("test");
        dto.setReversalDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}