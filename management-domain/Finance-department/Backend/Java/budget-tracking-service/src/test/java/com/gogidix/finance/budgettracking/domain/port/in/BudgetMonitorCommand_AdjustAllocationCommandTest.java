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
class BudgetMonitorCommand_AdjustAllocationCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorCommand.AdjustAllocationCommand dto = new BudgetMonitorCommand.AdjustAllocationCommand();
        dto.setTenantId("val-tenantId");
        dto.setMonitorId("val-monitorId");
        dto.setNewAllocation(BigDecimal.ONE);
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-monitorId", dto.getMonitorId());
        assertEquals(BigDecimal.ONE, dto.getNewAllocation());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorCommand.AdjustAllocationCommand dto1 = new BudgetMonitorCommand.AdjustAllocationCommand();
        BudgetMonitorCommand.AdjustAllocationCommand dto2 = new BudgetMonitorCommand.AdjustAllocationCommand();
        dto1.setTenantId("test");
        dto1.setMonitorId("test");
        dto1.setNewAllocation(BigDecimal.TEN);
        dto1.setUpdatedBy("test");
        dto2.setTenantId("test");
        dto2.setMonitorId("test");
        dto2.setNewAllocation(BigDecimal.TEN);
        dto2.setUpdatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorCommand.AdjustAllocationCommand dto = new BudgetMonitorCommand.AdjustAllocationCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setNewAllocation(BigDecimal.TEN);
        dto.setUpdatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorCommand.AdjustAllocationCommand dto = new BudgetMonitorCommand.AdjustAllocationCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setNewAllocation(BigDecimal.TEN);
        dto.setUpdatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}