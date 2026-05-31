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
class BudgetMonitorController_AdjustmentRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorController.AdjustmentRequestDto dto = new BudgetMonitorController.AdjustmentRequestDto();
        dto.setNewAllocation(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getNewAllocation());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorController.AdjustmentRequestDto dto1 = new BudgetMonitorController.AdjustmentRequestDto();
        BudgetMonitorController.AdjustmentRequestDto dto2 = new BudgetMonitorController.AdjustmentRequestDto();
        dto1.setNewAllocation(BigDecimal.TEN);
        dto2.setNewAllocation(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setNewAllocation(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorController.AdjustmentRequestDto dto = new BudgetMonitorController.AdjustmentRequestDto();
        dto.setNewAllocation(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorController.AdjustmentRequestDto dto = new BudgetMonitorController.AdjustmentRequestDto();
        dto.setNewAllocation(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}