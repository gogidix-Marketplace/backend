package com.gogidix.digitalmarketing.budgetmanagement.domain.model;

import com.gogidix.digitalmarketing.budgetmanagement.domain.model.Budget;
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
class Budget_BudgetPeriodTest {

        @Test
    void testSettersAndGetters() {
        Budget.BudgetPeriod dto = new Budget.BudgetPeriod();
        dto.setPeriod("val-period");
        dto.setAmount(BigDecimal.ONE);
        assertEquals("val-period", dto.getPeriod());
        assertEquals(BigDecimal.ONE, dto.getAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        Budget.BudgetPeriod dto1 = new Budget.BudgetPeriod();
        Budget.BudgetPeriod dto2 = new Budget.BudgetPeriod();
        dto1.setPeriod("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto2.setPeriod("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setPeriod(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        Budget.BudgetPeriod dto = new Budget.BudgetPeriod();
        dto.setPeriod("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setStartDate(null);
        dto.setEndDate(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        Budget.BudgetPeriod dto = new Budget.BudgetPeriod();
        dto.setPeriod("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setStartDate(null);
        dto.setEndDate(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}