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
class JournalEntryCommand_UpdateLineCommandTest {

        @Test
    void testSettersAndGetters() {
        JournalEntryCommand.UpdateLineCommand dto = new JournalEntryCommand.UpdateLineCommand();
        dto.setTenantId("val-tenantId");
        dto.setJournalEntryId("val-journalEntryId");
        dto.setLineId("val-lineId");
        dto.setDebitAmount(BigDecimal.ONE);
        dto.setCreditAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-journalEntryId", dto.getJournalEntryId());
        assertEquals("val-lineId", dto.getLineId());
        assertEquals(BigDecimal.ONE, dto.getDebitAmount());
        assertEquals(BigDecimal.ONE, dto.getCreditAmount());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryCommand.UpdateLineCommand dto1 = new JournalEntryCommand.UpdateLineCommand();
        JournalEntryCommand.UpdateLineCommand dto2 = new JournalEntryCommand.UpdateLineCommand();
        dto1.setTenantId("test");
        dto1.setJournalEntryId("test");
        dto1.setLineId("test");
        dto1.setDebitAmount(BigDecimal.TEN);
        dto1.setCreditAmount(BigDecimal.TEN);
        dto1.setDescription("test");
        dto2.setTenantId("test");
        dto2.setJournalEntryId("test");
        dto2.setLineId("test");
        dto2.setDebitAmount(BigDecimal.TEN);
        dto2.setCreditAmount(BigDecimal.TEN);
        dto2.setDescription("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        JournalEntryCommand.UpdateLineCommand dto = new JournalEntryCommand.UpdateLineCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
        dto.setLineId("test");
        dto.setDebitAmount(BigDecimal.TEN);
        dto.setCreditAmount(BigDecimal.TEN);
        dto.setDescription("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        JournalEntryCommand.UpdateLineCommand dto = new JournalEntryCommand.UpdateLineCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
        dto.setLineId("test");
        dto.setDebitAmount(BigDecimal.TEN);
        dto.setCreditAmount(BigDecimal.TEN);
        dto.setDescription("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}