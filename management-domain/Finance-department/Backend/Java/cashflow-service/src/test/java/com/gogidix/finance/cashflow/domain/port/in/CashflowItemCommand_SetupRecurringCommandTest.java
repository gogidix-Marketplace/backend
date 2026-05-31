package com.gogidix.finance.cashflow.domain.port.in;

import com.gogidix.finance.cashflow.domain.model.CashflowItem;
import com.gogidix.finance.cashflow.domain.port.in.CashflowItemCommand;
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
class CashflowItemCommand_SetupRecurringCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowItemCommand.SetupRecurringCommand dto = new CashflowItemCommand.SetupRecurringCommand();
        dto.setTenantId("val-tenantId");
        dto.setCashflowItemId("val-cashflowItemId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-cashflowItemId", dto.getCashflowItemId());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemCommand.SetupRecurringCommand dto1 = new CashflowItemCommand.SetupRecurringCommand();
        CashflowItemCommand.SetupRecurringCommand dto2 = new CashflowItemCommand.SetupRecurringCommand();
        dto1.setTenantId("test");
        dto1.setCashflowItemId("test");
        dto1.setFrequency(CashflowItem.RecurringFrequency.DAILY);
        dto2.setTenantId("test");
        dto2.setCashflowItemId("test");
        dto2.setFrequency(CashflowItem.RecurringFrequency.DAILY);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowItemCommand.SetupRecurringCommand dto = new CashflowItemCommand.SetupRecurringCommand();
        dto.setTenantId("test");
        dto.setCashflowItemId("test");
        dto.setFrequency(CashflowItem.RecurringFrequency.DAILY);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowItemCommand.SetupRecurringCommand dto = new CashflowItemCommand.SetupRecurringCommand();
        dto.setTenantId("test");
        dto.setCashflowItemId("test");
        dto.setFrequency(CashflowItem.RecurringFrequency.DAILY);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}