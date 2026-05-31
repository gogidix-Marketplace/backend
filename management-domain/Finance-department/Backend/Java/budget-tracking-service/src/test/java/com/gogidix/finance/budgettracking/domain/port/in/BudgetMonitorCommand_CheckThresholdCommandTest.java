package com.gogidix.finance.budgettracking.domain.port.in;

import com.gogidix.finance.budgettracking.domain.model.BudgetMonitor;
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
class BudgetMonitorCommand_CheckThresholdCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorCommand.CheckThresholdCommand dto = new BudgetMonitorCommand.CheckThresholdCommand();
        dto.setTenantId("val-tenantId");
        dto.setMonitorId("val-monitorId");
        dto.setThresholdType("val-thresholdType");
        dto.setThresholdValue(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-monitorId", dto.getMonitorId());
        assertEquals("val-thresholdType", dto.getThresholdType());
        assertEquals(BigDecimal.ONE, dto.getThresholdValue());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorCommand.CheckThresholdCommand dto1 = new BudgetMonitorCommand.CheckThresholdCommand();
        BudgetMonitorCommand.CheckThresholdCommand dto2 = new BudgetMonitorCommand.CheckThresholdCommand();
        dto1.setTenantId("test");
        dto1.setMonitorId("test");
        dto1.setThresholdType("test");
        dto1.setThresholdValue(BigDecimal.TEN);
        dto1.setLevel(BudgetMonitor.ThresholdLevel.INFO);
        dto2.setTenantId("test");
        dto2.setMonitorId("test");
        dto2.setThresholdType("test");
        dto2.setThresholdValue(BigDecimal.TEN);
        dto2.setLevel(BudgetMonitor.ThresholdLevel.INFO);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorCommand.CheckThresholdCommand dto = new BudgetMonitorCommand.CheckThresholdCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setThresholdType("test");
        dto.setThresholdValue(BigDecimal.TEN);
        dto.setLevel(BudgetMonitor.ThresholdLevel.INFO);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorCommand.CheckThresholdCommand dto = new BudgetMonitorCommand.CheckThresholdCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setThresholdType("test");
        dto.setThresholdValue(BigDecimal.TEN);
        dto.setLevel(BudgetMonitor.ThresholdLevel.INFO);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}