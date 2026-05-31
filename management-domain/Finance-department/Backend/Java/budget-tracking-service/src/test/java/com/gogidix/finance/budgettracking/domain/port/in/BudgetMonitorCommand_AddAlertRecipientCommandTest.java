package com.gogidix.finance.budgettracking.domain.port.in;

import com.gogidix.finance.budgettracking.domain.port.in.BudgetMonitorCommand;
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
class BudgetMonitorCommand_AddAlertRecipientCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorCommand.AddAlertRecipientCommand dto = new BudgetMonitorCommand.AddAlertRecipientCommand();
        dto.setTenantId("val-tenantId");
        dto.setMonitorId("val-monitorId");
        dto.setRecipient("val-recipient");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-monitorId", dto.getMonitorId());
        assertEquals("val-recipient", dto.getRecipient());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorCommand.AddAlertRecipientCommand dto1 = new BudgetMonitorCommand.AddAlertRecipientCommand();
        BudgetMonitorCommand.AddAlertRecipientCommand dto2 = new BudgetMonitorCommand.AddAlertRecipientCommand();
        dto1.setTenantId("test");
        dto1.setMonitorId("test");
        dto1.setRecipient("test");
        dto2.setTenantId("test");
        dto2.setMonitorId("test");
        dto2.setRecipient("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorCommand.AddAlertRecipientCommand dto = new BudgetMonitorCommand.AddAlertRecipientCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setRecipient("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorCommand.AddAlertRecipientCommand dto = new BudgetMonitorCommand.AddAlertRecipientCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setRecipient("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}