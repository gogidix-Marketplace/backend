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
class BudgetMonitorCommand_ThresholdConfigTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorCommand.ThresholdConfig dto = new BudgetMonitorCommand.ThresholdConfig();
        dto.setThresholdType("val-thresholdType");
        dto.setThresholdValue(BigDecimal.ONE);
        assertEquals("val-thresholdType", dto.getThresholdType());
        assertEquals(BigDecimal.ONE, dto.getThresholdValue());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorCommand.ThresholdConfig dto1 = new BudgetMonitorCommand.ThresholdConfig();
        BudgetMonitorCommand.ThresholdConfig dto2 = new BudgetMonitorCommand.ThresholdConfig();
        dto1.setThresholdType("test");
        dto1.setThresholdValue(BigDecimal.TEN);
        dto1.setLevel(BudgetMonitor.ThresholdLevel.INFO);
        dto2.setThresholdType("test");
        dto2.setThresholdValue(BigDecimal.TEN);
        dto2.setLevel(BudgetMonitor.ThresholdLevel.INFO);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setThresholdType(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorCommand.ThresholdConfig dto = new BudgetMonitorCommand.ThresholdConfig();
        dto.setThresholdType("test");
        dto.setThresholdValue(BigDecimal.TEN);
        dto.setLevel(BudgetMonitor.ThresholdLevel.INFO);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorCommand.ThresholdConfig dto = new BudgetMonitorCommand.ThresholdConfig();
        dto.setThresholdType("test");
        dto.setThresholdValue(BigDecimal.TEN);
        dto.setLevel(BudgetMonitor.ThresholdLevel.INFO);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}