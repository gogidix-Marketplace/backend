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
class JournalEntryCommand_JournalEntryLineDtoTest {

        @Test
    void testSettersAndGetters() {
        JournalEntryCommand.JournalEntryLineDto dto = new JournalEntryCommand.JournalEntryLineDto();
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setDebitAmount(BigDecimal.ONE);
        dto.setCreditAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        dto.setCostCenter("val-costCenter");
        dto.setDepartment("val-department");
        dto.setProjectId("val-projectId");
        dto.setTaskId("val-taskId");
        dto.setReference("val-reference");
        dto.setTaxCode("val-taxCode");
        dto.setTaxRate(BigDecimal.ONE);
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(BigDecimal.ONE, dto.getDebitAmount());
        assertEquals(BigDecimal.ONE, dto.getCreditAmount());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals("val-taskId", dto.getTaskId());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.ONE, dto.getTaxRate());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryCommand.JournalEntryLineDto dto1 = new JournalEntryCommand.JournalEntryLineDto();
        JournalEntryCommand.JournalEntryLineDto dto2 = new JournalEntryCommand.JournalEntryLineDto();
        dto1.setAccountId("test");
        dto1.setAccountNumber("test");
        dto1.setAccountName("test");
        dto1.setDebitAmount(BigDecimal.TEN);
        dto1.setCreditAmount(BigDecimal.TEN);
        dto1.setDescription("test");
        dto1.setCostCenter("test");
        dto1.setDepartment("test");
        dto1.setProjectId("test");
        dto1.setTaskId("test");
        dto1.setReference("test");
        dto1.setTaxCode("test");
        dto1.setTaxRate(BigDecimal.TEN);
        dto1.setTags(Collections.emptyList());
        dto2.setAccountId("test");
        dto2.setAccountNumber("test");
        dto2.setAccountName("test");
        dto2.setDebitAmount(BigDecimal.TEN);
        dto2.setCreditAmount(BigDecimal.TEN);
        dto2.setDescription("test");
        dto2.setCostCenter("test");
        dto2.setDepartment("test");
        dto2.setProjectId("test");
        dto2.setTaskId("test");
        dto2.setReference("test");
        dto2.setTaxCode("test");
        dto2.setTaxRate(BigDecimal.TEN);
        dto2.setTags(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAccountId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        JournalEntryCommand.JournalEntryLineDto dto = new JournalEntryCommand.JournalEntryLineDto();
        dto.setAccountId("test");
        dto.setAccountNumber("test");
        dto.setAccountName("test");
        dto.setDebitAmount(BigDecimal.TEN);
        dto.setCreditAmount(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setCostCenter("test");
        dto.setDepartment("test");
        dto.setProjectId("test");
        dto.setTaskId("test");
        dto.setReference("test");
        dto.setTaxCode("test");
        dto.setTaxRate(BigDecimal.TEN);
        dto.setTags(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        JournalEntryCommand.JournalEntryLineDto dto = new JournalEntryCommand.JournalEntryLineDto();
        dto.setAccountId("test");
        dto.setAccountNumber("test");
        dto.setAccountName("test");
        dto.setDebitAmount(BigDecimal.TEN);
        dto.setCreditAmount(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setCostCenter("test");
        dto.setDepartment("test");
        dto.setProjectId("test");
        dto.setTaskId("test");
        dto.setReference("test");
        dto.setTaxCode("test");
        dto.setTaxRate(BigDecimal.TEN);
        dto.setTags(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}