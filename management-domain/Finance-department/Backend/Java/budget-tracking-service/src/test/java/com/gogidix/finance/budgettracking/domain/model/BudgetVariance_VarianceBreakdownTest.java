package com.gogidix.finance.budgettracking.domain.model;

import com.gogidix.finance.budgettracking.domain.model.BudgetVariance;
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
class BudgetVariance_VarianceBreakdownTest {

        @Test
    void testBuilder() {
        BudgetVariance.VarianceBreakdown dto = BudgetVariance.VarianceBreakdown.builder()
                        .category("test-category")
            .description("test-description")
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .contribution("test-contribution")
            .build();
        assertNotNull(dto);
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getBudgetedAmount());
        assertEquals(BigDecimal.TEN, dto.getActualAmount());
        assertEquals(BigDecimal.TEN, dto.getVarianceAmount());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
        assertEquals("test-contribution", dto.getContribution());
    }

    @Test
    void testSettersAndGetters() {
        BudgetVariance.VarianceBreakdown dto = new BudgetVariance.VarianceBreakdown();
        dto.setCategory("val-category");
        dto.setDescription("val-description");
        dto.setBudgetedAmount(BigDecimal.ONE);
        dto.setActualAmount(BigDecimal.ONE);
        dto.setVarianceAmount(BigDecimal.ONE);
        dto.setVariancePercentage(BigDecimal.ONE);
        dto.setContribution("val-contribution");
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getBudgetedAmount());
        assertEquals(BigDecimal.ONE, dto.getActualAmount());
        assertEquals(BigDecimal.ONE, dto.getVarianceAmount());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
        assertEquals("val-contribution", dto.getContribution());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetVariance.VarianceBreakdown dto1 = BudgetVariance.VarianceBreakdown.builder()
                        .category("test-category")
            .description("test-description")
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .contribution("test-contribution")
            .build();
        BudgetVariance.VarianceBreakdown dto2 = BudgetVariance.VarianceBreakdown.builder()
                        .category("test-category")
            .description("test-description")
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .contribution("test-contribution")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetVariance.VarianceBreakdown dto = BudgetVariance.VarianceBreakdown.builder()
                        .category("test-category")
            .description("test-description")
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .contribution("test-contribution")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}