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
class JournalEntryCommand_SubmitForApprovalCommandTest {

        @Test
    void testSettersAndGetters() {
        JournalEntryCommand.SubmitForApprovalCommand dto = new JournalEntryCommand.SubmitForApprovalCommand();
        dto.setTenantId("val-tenantId");
        dto.setJournalEntryId("val-journalEntryId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-journalEntryId", dto.getJournalEntryId());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryCommand.SubmitForApprovalCommand dto1 = new JournalEntryCommand.SubmitForApprovalCommand();
        JournalEntryCommand.SubmitForApprovalCommand dto2 = new JournalEntryCommand.SubmitForApprovalCommand();
        dto1.setTenantId("test");
        dto1.setJournalEntryId("test");
        dto2.setTenantId("test");
        dto2.setJournalEntryId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        JournalEntryCommand.SubmitForApprovalCommand dto = new JournalEntryCommand.SubmitForApprovalCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        JournalEntryCommand.SubmitForApprovalCommand dto = new JournalEntryCommand.SubmitForApprovalCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}