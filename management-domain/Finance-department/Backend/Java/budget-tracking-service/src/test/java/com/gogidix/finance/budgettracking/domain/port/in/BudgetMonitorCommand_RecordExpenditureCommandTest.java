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
class BudgetMonitorCommand_RecordExpenditureCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorCommand.RecordExpenditureCommand dto = new BudgetMonitorCommand.RecordExpenditureCommand();
        dto.setTenantId("val-tenantId");
        dto.setMonitorId("val-monitorId");
        dto.setAmount(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-monitorId", dto.getMonitorId());
        assertEquals(BigDecimal.ONE, dto.getAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorCommand.RecordExpenditureCommand dto1 = new BudgetMonitorCommand.RecordExpenditureCommand();
        BudgetMonitorCommand.RecordExpenditureCommand dto2 = new BudgetMonitorCommand.RecordExpenditureCommand();
        dto1.setTenantId("test");
        dto1.setMonitorId("test");
        dto1.setAmount(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setMonitorId("test");
        dto2.setAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorCommand.RecordExpenditureCommand dto = new BudgetMonitorCommand.RecordExpenditureCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorCommand.RecordExpenditureCommand dto = new BudgetMonitorCommand.RecordExpenditureCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}