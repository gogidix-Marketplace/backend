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
class CashflowItemCommand_SettleCashflowItemCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowItemCommand.SettleCashflowItemCommand dto = new CashflowItemCommand.SettleCashflowItemCommand();
        dto.setTenantId("val-tenantId");
        dto.setCashflowItemId("val-cashflowItemId");
        dto.setBankReference("val-bankReference");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-cashflowItemId", dto.getCashflowItemId());
        assertEquals("val-bankReference", dto.getBankReference());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemCommand.SettleCashflowItemCommand dto1 = new CashflowItemCommand.SettleCashflowItemCommand();
        CashflowItemCommand.SettleCashflowItemCommand dto2 = new CashflowItemCommand.SettleCashflowItemCommand();
        dto1.setTenantId("test");
        dto1.setCashflowItemId("test");
        dto1.setBankReference("test");
        dto2.setTenantId("test");
        dto2.setCashflowItemId("test");
        dto2.setBankReference("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowItemCommand.SettleCashflowItemCommand dto = new CashflowItemCommand.SettleCashflowItemCommand();
        dto.setTenantId("test");
        dto.setCashflowItemId("test");
        dto.setBankReference("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowItemCommand.SettleCashflowItemCommand dto = new CashflowItemCommand.SettleCashflowItemCommand();
        dto.setTenantId("test");
        dto.setCashflowItemId("test");
        dto.setBankReference("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}