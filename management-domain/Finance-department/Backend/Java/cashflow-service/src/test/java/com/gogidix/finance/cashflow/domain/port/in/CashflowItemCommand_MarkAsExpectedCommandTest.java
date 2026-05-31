package com.gogidix.finance.cashflow.domain.port.in;

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
class CashflowItemCommand_MarkAsExpectedCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowItemCommand.MarkAsExpectedCommand dto = new CashflowItemCommand.MarkAsExpectedCommand();
        dto.setTenantId("val-tenantId");
        dto.setCashflowItemId("val-cashflowItemId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-cashflowItemId", dto.getCashflowItemId());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemCommand.MarkAsExpectedCommand dto1 = new CashflowItemCommand.MarkAsExpectedCommand();
        CashflowItemCommand.MarkAsExpectedCommand dto2 = new CashflowItemCommand.MarkAsExpectedCommand();
        dto1.setTenantId("test");
        dto1.setCashflowItemId("test");
        dto2.setTenantId("test");
        dto2.setCashflowItemId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowItemCommand.MarkAsExpectedCommand dto = new CashflowItemCommand.MarkAsExpectedCommand();
        dto.setTenantId("test");
        dto.setCashflowItemId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowItemCommand.MarkAsExpectedCommand dto = new CashflowItemCommand.MarkAsExpectedCommand();
        dto.setTenantId("test");
        dto.setCashflowItemId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}