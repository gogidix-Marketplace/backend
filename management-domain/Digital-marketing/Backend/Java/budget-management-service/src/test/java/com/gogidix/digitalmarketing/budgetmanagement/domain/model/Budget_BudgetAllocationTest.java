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
class Budget_BudgetAllocationTest {

        @Test
    void testSettersAndGetters() {
        Budget.BudgetAllocation dto = new Budget.BudgetAllocation();
        dto.setCategory("val-category");
        dto.setAmount(BigDecimal.ONE);
        dto.setStartDate("val-startDate");
        dto.setEndDate("val-endDate");
        dto.setDescription("val-description");
        assertEquals("val-category", dto.getCategory());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-startDate", dto.getStartDate());
        assertEquals("val-endDate", dto.getEndDate());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        Budget.BudgetAllocation dto1 = new Budget.BudgetAllocation();
        Budget.BudgetAllocation dto2 = new Budget.BudgetAllocation();
        dto1.setCategory("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setStartDate("test");
        dto1.setEndDate("test");
        dto1.setDescription("test");
        dto2.setCategory("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setStartDate("test");
        dto2.setEndDate("test");
        dto2.setDescription("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCategory(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        Budget.BudgetAllocation dto = new Budget.BudgetAllocation();
        dto.setCategory("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setStartDate("test");
        dto.setEndDate("test");
        dto.setDescription("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        Budget.BudgetAllocation dto = new Budget.BudgetAllocation();
        dto.setCategory("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setStartDate("test");
        dto.setEndDate("test");
        dto.setDescription("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}