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
class CashflowItemCommand_CancelCashflowItemCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowItemCommand.CancelCashflowItemCommand dto = new CashflowItemCommand.CancelCashflowItemCommand();
        dto.setTenantId("val-tenantId");
        dto.setCashflowItemId("val-cashflowItemId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-cashflowItemId", dto.getCashflowItemId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemCommand.CancelCashflowItemCommand dto1 = new CashflowItemCommand.CancelCashflowItemCommand();
        CashflowItemCommand.CancelCashflowItemCommand dto2 = new CashflowItemCommand.CancelCashflowItemCommand();
        dto1.setTenantId("test");
        dto1.setCashflowItemId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setCashflowItemId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowItemCommand.CancelCashflowItemCommand dto = new CashflowItemCommand.CancelCashflowItemCommand();
        dto.setTenantId("test");
        dto.setCashflowItemId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowItemCommand.CancelCashflowItemCommand dto = new CashflowItemCommand.CancelCashflowItemCommand();
        dto.setTenantId("test");
        dto.setCashflowItemId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}