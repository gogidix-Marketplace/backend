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
class BudgetMonitorController_ThresholdAcknowledgeRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorController.ThresholdAcknowledgeRequestDto dto = new BudgetMonitorController.ThresholdAcknowledgeRequestDto();
        dto.setThresholdType("val-thresholdType");
        assertEquals("val-thresholdType", dto.getThresholdType());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorController.ThresholdAcknowledgeRequestDto dto1 = new BudgetMonitorController.ThresholdAcknowledgeRequestDto();
        BudgetMonitorController.ThresholdAcknowledgeRequestDto dto2 = new BudgetMonitorController.ThresholdAcknowledgeRequestDto();
        dto1.setThresholdType("test");
        dto2.setThresholdType("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setThresholdType(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorController.ThresholdAcknowledgeRequestDto dto = new BudgetMonitorController.ThresholdAcknowledgeRequestDto();
        dto.setThresholdType("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorController.ThresholdAcknowledgeRequestDto dto = new BudgetMonitorController.ThresholdAcknowledgeRequestDto();
        dto.setThresholdType("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}