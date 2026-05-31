package com.gogidix.finance.budgettracking.interfaces.rest;

import com.gogidix.finance.budgettracking.interfaces.rest.BudgetMonitorController;
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
class BudgetMonitorController_ThresholdCheckRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorController.ThresholdCheckRequestDto dto = new BudgetMonitorController.ThresholdCheckRequestDto();
        dto.setThresholdType("val-thresholdType");
        dto.setThresholdValue(BigDecimal.ONE);
        dto.setLevel("val-level");
        assertEquals("val-thresholdType", dto.getThresholdType());
        assertEquals(BigDecimal.ONE, dto.getThresholdValue());
        assertEquals("val-level", dto.getLevel());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorController.ThresholdCheckRequestDto dto1 = new BudgetMonitorController.ThresholdCheckRequestDto();
        BudgetMonitorController.ThresholdCheckRequestDto dto2 = new BudgetMonitorController.ThresholdCheckRequestDto();
        dto1.setThresholdType("test");
        dto1.setThresholdValue(BigDecimal.TEN);
        dto1.setLevel("test");
        dto2.setThresholdType("test");
        dto2.setThresholdValue(BigDecimal.TEN);
        dto2.setLevel("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setThresholdType(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorController.ThresholdCheckRequestDto dto = new BudgetMonitorController.ThresholdCheckRequestDto();
        dto.setThresholdType("test");
        dto.setThresholdValue(BigDecimal.TEN);
        dto.setLevel("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorController.ThresholdCheckRequestDto dto = new BudgetMonitorController.ThresholdCheckRequestDto();
        dto.setThresholdType("test");
        dto.setThresholdValue(BigDecimal.TEN);
        dto.setLevel("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}