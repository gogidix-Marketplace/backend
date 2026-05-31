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
class BudgetMonitorCommand_UpdateMonitorCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorCommand.UpdateMonitorCommand dto = new BudgetMonitorCommand.UpdateMonitorCommand();
        dto.setTenantId("val-tenantId");
        dto.setMonitorId("val-monitorId");
        dto.setBudgetName("val-budgetName");
        dto.setAllocatedAmount(BigDecimal.ONE);
        dto.setCategory("val-category");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-monitorId", dto.getMonitorId());
        assertEquals("val-budgetName", dto.getBudgetName());
        assertEquals(BigDecimal.ONE, dto.getAllocatedAmount());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorCommand.UpdateMonitorCommand dto1 = new BudgetMonitorCommand.UpdateMonitorCommand();
        BudgetMonitorCommand.UpdateMonitorCommand dto2 = new BudgetMonitorCommand.UpdateMonitorCommand();
        dto1.setTenantId("test");
        dto1.setMonitorId("test");
        dto1.setBudgetName("test");
        dto1.setAllocatedAmount(BigDecimal.TEN);
        dto1.setCategory("test");
        dto1.setDepartment("test");
        dto1.setCostCenter("test");
        dto2.setTenantId("test");
        dto2.setMonitorId("test");
        dto2.setBudgetName("test");
        dto2.setAllocatedAmount(BigDecimal.TEN);
        dto2.setCategory("test");
        dto2.setDepartment("test");
        dto2.setCostCenter("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorCommand.UpdateMonitorCommand dto = new BudgetMonitorCommand.UpdateMonitorCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setBudgetName("test");
        dto.setAllocatedAmount(BigDecimal.TEN);
        dto.setCategory("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorCommand.UpdateMonitorCommand dto = new BudgetMonitorCommand.UpdateMonitorCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setBudgetName("test");
        dto.setAllocatedAmount(BigDecimal.TEN);
        dto.setCategory("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}