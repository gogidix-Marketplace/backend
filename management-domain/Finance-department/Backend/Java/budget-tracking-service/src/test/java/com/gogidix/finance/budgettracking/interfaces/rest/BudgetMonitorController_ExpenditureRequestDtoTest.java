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
class BudgetMonitorController_ExpenditureRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorController.ExpenditureRequestDto dto = new BudgetMonitorController.ExpenditureRequestDto();
        dto.setAmount(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorController.ExpenditureRequestDto dto1 = new BudgetMonitorController.ExpenditureRequestDto();
        BudgetMonitorController.ExpenditureRequestDto dto2 = new BudgetMonitorController.ExpenditureRequestDto();
        dto1.setAmount(BigDecimal.TEN);
        dto2.setAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAmount(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorController.ExpenditureRequestDto dto = new BudgetMonitorController.ExpenditureRequestDto();
        dto.setAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorController.ExpenditureRequestDto dto = new BudgetMonitorController.ExpenditureRequestDto();
        dto.setAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}