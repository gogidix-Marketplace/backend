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
class BudgetMonitorCommand_AcknowledgeThresholdCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorCommand.AcknowledgeThresholdCommand dto = new BudgetMonitorCommand.AcknowledgeThresholdCommand();
        dto.setTenantId("val-tenantId");
        dto.setMonitorId("val-monitorId");
        dto.setThresholdType("val-thresholdType");
        dto.setAcknowledgedBy("val-acknowledgedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-monitorId", dto.getMonitorId());
        assertEquals("val-thresholdType", dto.getThresholdType());
        assertEquals("val-acknowledgedBy", dto.getAcknowledgedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorCommand.AcknowledgeThresholdCommand dto1 = new BudgetMonitorCommand.AcknowledgeThresholdCommand();
        BudgetMonitorCommand.AcknowledgeThresholdCommand dto2 = new BudgetMonitorCommand.AcknowledgeThresholdCommand();
        dto1.setTenantId("test");
        dto1.setMonitorId("test");
        dto1.setThresholdType("test");
        dto1.setAcknowledgedBy("test");
        dto2.setTenantId("test");
        dto2.setMonitorId("test");
        dto2.setThresholdType("test");
        dto2.setAcknowledgedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorCommand.AcknowledgeThresholdCommand dto = new BudgetMonitorCommand.AcknowledgeThresholdCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setThresholdType("test");
        dto.setAcknowledgedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorCommand.AcknowledgeThresholdCommand dto = new BudgetMonitorCommand.AcknowledgeThresholdCommand();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        dto.setThresholdType("test");
        dto.setAcknowledgedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}