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
class JournalEntryCommand_AddLineCommandTest {

        @Test
    void testSettersAndGetters() {
        JournalEntryCommand.AddLineCommand dto = new JournalEntryCommand.AddLineCommand();
        dto.setTenantId("val-tenantId");
        dto.setJournalEntryId("val-journalEntryId");
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setDebitAmount(BigDecimal.ONE);
        dto.setCreditAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        dto.setCostCenter("val-costCenter");
        dto.setDepartment("val-department");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-journalEntryId", dto.getJournalEntryId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(BigDecimal.ONE, dto.getDebitAmount());
        assertEquals(BigDecimal.ONE, dto.getCreditAmount());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-department", dto.getDepartment());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryCommand.AddLineCommand dto1 = new JournalEntryCommand.AddLineCommand();
        JournalEntryCommand.AddLineCommand dto2 = new JournalEntryCommand.AddLineCommand();
        dto1.setTenantId("test");
        dto1.setJournalEntryId("test");
        dto1.setAccountId("test");
        dto1.setAccountNumber("test");
        dto1.setAccountName("test");
        dto1.setDebitAmount(BigDecimal.TEN);
        dto1.setCreditAmount(BigDecimal.TEN);
        dto1.setDescription("test");
        dto1.setCostCenter("test");
        dto1.setDepartment("test");
        dto2.setTenantId("test");
        dto2.setJournalEntryId("test");
        dto2.setAccountId("test");
        dto2.setAccountNumber("test");
        dto2.setAccountName("test");
        dto2.setDebitAmount(BigDecimal.TEN);
        dto2.setCreditAmount(BigDecimal.TEN);
        dto2.setDescription("test");
        dto2.setCostCenter("test");
        dto2.setDepartment("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        JournalEntryCommand.AddLineCommand dto = new JournalEntryCommand.AddLineCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
        dto.setAccountId("test");
        dto.setAccountNumber("test");
        dto.setAccountName("test");
        dto.setDebitAmount(BigDecimal.TEN);
        dto.setCreditAmount(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setCostCenter("test");
        dto.setDepartment("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        JournalEntryCommand.AddLineCommand dto = new JournalEntryCommand.AddLineCommand();
        dto.setTenantId("test");
        dto.setJournalEntryId("test");
        dto.setAccountId("test");
        dto.setAccountNumber("test");
        dto.setAccountName("test");
        dto.setDebitAmount(BigDecimal.TEN);
        dto.setCreditAmount(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setCostCenter("test");
        dto.setDepartment("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}